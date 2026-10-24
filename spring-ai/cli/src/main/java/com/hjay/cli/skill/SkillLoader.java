package com.hjay.cli.skill;

import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SKILL.md 加载器。
 * <p>
 * 为什么不用 {@code org.springaicommunity.agent.utils.Skills}：它内部的 {@code MarkdownParser}
 * 不是 YAML 解析器，只是按行、按第一个冒号切 key/value，因此
 * <ul>
 * <li>不支持 {@code >}、{@code |} 多行值（只会得到字面量 {@code >}）</li>
 * <li>值里出现 {@code : } 会被截断，且不做引号内转义</li>
 * <li>不支持列表（{@code allowed-tools:} 这类写法）</li>
 * <li>闭合分隔符用 {@code indexOf("---", 3)}，正文里出现 {@code ---} 会提前截断</li>
 * </ul>
 * 这里用 SnakeYAML（Spring Boot 已带，无需新增依赖）解析 front matter，再转成库里的
 * {@link SkillsTool.Skill} 记录，工具名称、调用返回格式、描述模板都与原版保持一致。
 */
public final class SkillLoader {

    private static final String SKILL_FILE = "SKILL.md";

    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z0-9][A-Za-z0-9._-]*");

    private SkillLoader() {
    }

    /**
     * 扫描 classpath 下某个前缀里的所有 SKILL.md（支持 target/classes 与 jar 包内）。
     * @param prefix 形如 {@code skills}，对应 {@code src/main/resources/skills}
     */
    public static List<SkillsTool.Skill> loadFromClasspath(String prefix) {
        Resource[] resources;
        try {
            resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath*:" + prefix + "/**/" + SKILL_FILE);
        }
        catch (IOException e) {
            throw new UncheckedIOException("扫描 classpath:" + prefix + " 失败", e);
        }

        List<SkillsTool.Skill> skills = new ArrayList<>();
        // 排序保证技能清单顺序稳定，便于排查
        List<Resource> ordered = Arrays.stream(resources)
            .sorted((a, b) -> a.getDescription().compareTo(b.getDescription()))
            .toList();
        for (Resource resource : ordered) {
            try (var in = resource.getInputStream()) {
                String markdown = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                skills.add(parse(markdown, basePathOf(resource)));
            }
            catch (IOException e) {
                throw new UncheckedIOException("读取技能文件失败：" + resource.getDescription(), e);
            }
        }
        return skills;
    }

    /**
     * 扫描文件系统目录里的所有 SKILL.md。
     */
    public static List<SkillsTool.Skill> loadFromDirectory(Path root) {
        if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException("技能目录不存在或不是目录：" + root);
        }
        try (Stream<Path> paths = Files.walk(root)) {
            List<Path> files = paths.filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().equals(SKILL_FILE))
                .sorted()
                .toList();
            List<SkillsTool.Skill> skills = new ArrayList<>();
            for (Path file : files) {
                skills.add(parse(Files.readString(file, StandardCharsets.UTF_8),
                        file.getParent().toAbsolutePath().toString()));
            }
            return skills;
        }
        catch (IOException e) {
            throw new UncheckedIOException("读取技能目录失败：" + root, e);
        }
    }

    /**
     * 解析一个 SKILL.md：YAML front matter + 正文。
     * @param markdown 文件全文
     * @param basePath 技能所在目录，调用工具时回传给模型
     */
    public static SkillsTool.Skill parse(String markdown, String basePath) {
        String text = markdown.replace("\r\n", "\n").replace("\r", "\n");
        if (text.startsWith("\uFEFF")) {
            // Windows 记事本/部分编辑器会写 BOM，原版解析器会因此认不出 front matter
            text = text.substring(1);
        }

        List<String> lines = Arrays.asList(text.split("\n", -1));
        if (lines.isEmpty() || !lines.get(0).trim().equals("---")) {
            throw invalid(basePath, "文件必须以 '---' 开头（YAML front matter），实际首行是 [" + firstLine(lines) + "]");
        }

        int end = -1;
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.equals("---") || line.equals("...")) {
                end = i;
                break;
            }
        }
        if (end < 0) {
            throw invalid(basePath, "front matter 缺少结束的 '---'");
        }

        Map<String, Object> frontMatter = parseYaml(String.join("\n", lines.subList(1, end)), basePath);
        String content = String.join("\n", lines.subList(end + 1, lines.size())).trim();

        String name = requireScalar(frontMatter, "name", basePath);
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw invalid(basePath, "name 只能包含字母、数字、点、下划线和中划线，实际是 [" + name + "]");
        }
        requireScalar(frontMatter, "description", basePath);

        if (content.isEmpty()) {
            throw invalid(basePath, "front matter 之后没有正文，技能无法生效");
        }

        return new SkillsTool.Skill(basePath, frontMatter, content);
    }

    private static Map<String, Object> parseYaml(String yamlText, String basePath) {
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        options.setCodePointLimit(1024 * 1024);

        Object parsed;
        try {
            parsed = new Yaml(new SafeConstructor(options)).load(yamlText);
        }
        catch (RuntimeException e) {
            throw invalid(basePath, "front matter 不是合法 YAML：" + e.getMessage());
        }

        Map<String, Object> result = new LinkedHashMap<>();
        if (parsed == null) {
            return result;
        }
        if (!(parsed instanceof Map<?, ?> map)) {
            throw invalid(basePath, "front matter 必须是 key: value 映射，实际解析出 " + parsed.getClass().getSimpleName());
        }
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            result.put(String.valueOf(entry.getKey()), stringify(entry.getKey(), entry.getValue(), basePath));
        }
        return result;
    }

    /**
     * front matter 的值最终会被 {@link SkillsTool.Skill#toXml()} 直接拼进工具描述，
     * 所以这里统一拍平成单行字符串：标量折叠换行、列表用逗号连接、嵌套结构直接报错。
     */
    private static String stringify(Object key, Object value, String basePath) {
        if (value == null) {
            return "";
        }
        if (value instanceof Map<?, ?>) {
            throw invalid(basePath, "front matter 的 " + key + " 不支持嵌套映射");
        }
        if (value instanceof List<?> list) {
            return list.stream()
                .map(item -> item instanceof Map ? "{}" : oneLine(String.valueOf(item)))
                .collect(Collectors.joining(", "));
        }
        return oneLine(String.valueOf(value));
    }

    /**
     * YAML 的 {@code >} / {@code |} 折叠块会带换行，这里压成一行，
     * 避免换行被原样拼进 {@code <description>} 里。
     */
    private static String oneLine(String value) {
        return value.trim().replaceAll("\\s*\\n\\s*", " ");
    }

    private static String requireScalar(Map<String, Object> frontMatter, String key, String basePath) {
        Object value = frontMatter.get(key);
        if (value == null || String.valueOf(value).isBlank()) {
            throw invalid(basePath, "front matter 缺少必填字段 " + key);
        }
        return String.valueOf(value).trim();
    }

    private static String basePathOf(Resource resource) throws IOException {
        URL url = resource.getURL();
        if ("file".equals(url.getProtocol())) {
            try {
                Path file = Path.of(url.toURI());
                return file.getParent().toAbsolutePath().toString();
            }
            catch (Exception e) {
                throw new IOException("无法解析技能文件路径：" + url, e);
            }
        }
        // jar:file:/app.jar!/BOOT-INF/classes!/skills/x/SKILL.md -> BOOT-INF/classes!/skills/x
        String path = url.toString();
        int cut = path.lastIndexOf("/" + SKILL_FILE);
        if (cut > 0) {
            path = path.substring(0, cut);
        }
        int bang = path.lastIndexOf("!/");
        if (bang > 0) {
            path = path.substring(bang + 2);
        }
        return path;
    }

    private static String firstLine(List<String> lines) {
        return lines.isEmpty() ? "" : lines.get(0).trim();
    }

    private static IllegalArgumentException invalid(String basePath, String message) {
        return new IllegalArgumentException("技能文件 " + basePath + "/" + SKILL_FILE + " 有误：" + message);
    }
}
