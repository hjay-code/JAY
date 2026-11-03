package com.hjay.netty.demo.heima.c3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 目录和文件拷贝
 */
public class TestFileCopy {

    public static void main(String[] args) throws IOException {
        String source = "D:\\source";
        String target = "D:\\target";

        Files.walk(Paths.get(source)).forEach(path -> {
            try {
                String targetName = path.toString().replace(source, target);
                if (Files.isDirectory(path)){// 文件夹
                    Files.createDirectory(Paths.get(targetName));
                }else if (Files.isRegularFile(path)){// 普通文件
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
