package ru.eltech.mapeshkov.mlib;

import org.apache.spark.sql.Dataset;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyFileWriter implements AutoCloseable {
    private PrintWriter writer;
    private static String projectDir = System.getProperty("user.dir");

    public MyFileWriter() {
        this(Paths.get(projectDir + "\\Maksim\\src\\test\\resources\\mlib Ml out.txt"));
    }

    public MyFileWriter(Path path) {
        try {
            File parentFile = path.toFile().getParentFile();
            if (!parentFile.exists() && !parentFile.mkdirs())
                throw new IllegalStateException("Couldn't create dir: " + parentFile);
            writer = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(path.toFile()), StandardCharsets.UTF_8)
                    ), true);
            writer.print("");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void println(Object obj) {
        writer.println(obj);
    }

    public void println() {
        writer.println();
    }

    public void print(Object obj) {
        writer.print(obj);
        writer.flush();
    }

    public <T> void printSchema(Dataset<T> data) {
        println(data.schema().treeString());
    }

    public <T> void show(Dataset<T> data) {
        show(data, 5);
    }

    public <T> void show(Dataset<T> data, int numRows) {
        show(data, numRows, 100);
    }

    public <T> void show(Dataset<T> data, int numRows, int truncate) {
        println(data.showString(numRows, truncate, true));
    }

    @Override
    public void close() {
        writer.close();
    }
}