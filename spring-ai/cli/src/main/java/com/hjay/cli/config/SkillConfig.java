package com.hjay.cli.config;

import com.hjay.cli.skill.SkillLoader;

import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 技能（SKILL.md）注册。
 * <p>
 * 默认从 classpath:/skills 加载，即 src/main/resources/skills/&lt;skill-name&gt;/SKILL.md；
 * 配置了 app.skills.dir 时额外加载该目录下的技能（便于放公司内网共享目录或本地调试）。
 * 必须至少有一个技能，否则启动即失败——这是有意的，避免模型拿到空的技能清单。
 * <p>
 * 这里没有用 {@code SkillsTool.builder()}，而是自己解析 front matter 后用
 * {@link SkillsTool.SkillsFunction} 组装工具：工具名、入参、调用返回格式都与原版一致，
 * 只是把解析器换成了真正的 YAML（见 {@link SkillLoader}）。
 */
@Configuration
public class SkillConfig {

    /**
     * 与 spring-ai-agent-utils 0.7.0 的 SkillsTool.TOOL_DESCRIPTION_TEMPLATE 保持一致。
     * 原常量是 private，无法直接引用；升级依赖时需要同步这一份。
     */
    private static final String TOOL_DESCRIPTION_TEMPLATE = """
            Execute a skill within the main conversation

            <skills_instructions>
            When users ask you to perform tasks, check if any of the available skills below can help complete the task more effectively. Skills provide specialized capabilities and domain knowledge.

            How to use skills:
            - Invoke skills using this tool with the skill name only (no arguments)
            - When you invoke a skill, you will see <command-message>The "{name}" skill is loading</command-message>
            - The skill's prompt will expand and provide detailed instructions on how to complete the task

            NOTE: Response always starts start with the base directory of the skill execution environment. You can use this to retrieve additional files of call shell commands.
            Skill description follows after the base directory line.

            Important:
            - Only use skills listed in <available_skills> below
            - Do not invoke a skill that is already running
            </skills_instructions>

            <available_skills>
            %s
            </available_skills>
            """;

    @Bean
    ToolCallback skillTool(@Value("${app.skills.dir:}") String skillDir) {
        List<SkillsTool.Skill> skills = new ArrayList<>(SkillLoader.loadFromClasspath("skills"));

        if (StringUtils.hasText(skillDir)) {
            skills.addAll(SkillLoader.loadFromDirectory(Path.of(skillDir.trim())));
        }

        if (skills.isEmpty()) {
            throw new IllegalStateException(
                    "没有加载到任何技能，请在 src/main/resources/skills/<skill-name>/SKILL.md 放置技能文件");
        }

        Map<String, SkillsTool.Skill> skillsByName = new LinkedHashMap<>();
        for (SkillsTool.Skill skill : skills) {
            String name = skill.name();
            SkillsTool.Skill previous = skillsByName.putIfAbsent(name, skill);
            if (previous != null) {
                throw new IllegalStateException("技能名重复：[" + name + "] 同时出现在 " + previous.basePath()
                        + " 和 " + skill.basePath() + "，请改名");
            }
        }

        String skillsXml = skillsByName.values()
            .stream()
            .map(SkillsTool.Skill::toXml)
            .collect(Collectors.joining("\n"));

        return FunctionToolCallback.builder("Skill", new SkillsTool.SkillsFunction(skillsByName))
            .description(TOOL_DESCRIPTION_TEMPLATE.formatted(skillsXml))
            .inputType(SkillsTool.SkillsInput.class)
            .build();
    }
}
