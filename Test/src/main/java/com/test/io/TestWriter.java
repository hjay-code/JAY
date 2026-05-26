package com.test.io;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

@Slf4j
public class TestWriter {


    public static void write(String context, String path, String charset) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path), charset))) {
            writer.write(context);
            log.info("write file={} success", path);
        } catch (java.io.IOException e) {
            log.error("write file={} error:{}",path, e.getMessage());
        }
    }

        public static void main(String[] args) {
            String context = "Hello, World!";
            String path = "output.txt";
            String charset = "UTF-8";
            write(context, path, charset);
        }
}
