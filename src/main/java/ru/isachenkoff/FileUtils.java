package ru.isachenkoff;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {
    private static final String INPUT_DIR_NAME = "input";
    private static final String OUTPUT_DIR_NAME = "output";
    private static final Pattern fileNamePattern = Pattern.compile("(.*/)?(?<name>.*)\\.(?<ext>.*)");
    
    public static File getInputFile() {
        File[] files = new File(INPUT_DIR_NAME).listFiles();
        if (files == null || files.length == 0) {
            throw new IllegalStateException("Input directory is empty");
        }
        if (files.length > 1) {
            throw new IllegalStateException("More than one file in the input directory");
        }
        return files[0];
    }
    
    public static String getName(File file) {
        Matcher matcher = fileNamePattern.matcher(file.getName());
        if (matcher.find()) {
            return matcher.group("name");
        } else {
            return "";
        }
    }
    
    public static String getExt(File file) {
        Matcher matcher = fileNamePattern.matcher(file.getName());
        if (matcher.find()) {
            return matcher.group("ext");
        } else {
            return "";
        }
    }
    
    public static void writeCsv(String oldName, String result) {
        try {
            Files.writeString(new File(OUTPUT_DIR_NAME, oldName + "r.csv").toPath(), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
