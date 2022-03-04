package ru.isachenkoff;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Converter {
    private static final String INPUT_DIR_NAME = "input";
    private static final String OUTPUT_DIR_NAME = "output";
    private static final Pattern fileNamePattern = Pattern.compile("(.*/)?(?<name>.*)\\.(?<ext>.*)");
    
    public static void main(String[] args) {
        File inputFile = getInputFile();
        String converted;
        if (inputFile.getName().toLowerCase().endsWith(".txt")) {
            try {
                converted = convertFromTxt(inputFile);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            throw new RuntimeException("Unexpected file type");
        }
        try {
            Files.writeString(new File(OUTPUT_DIR_NAME, getName(inputFile) + "r.csv").toPath(), converted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static File getInputFile() {
        File[] files = new File(INPUT_DIR_NAME).listFiles();
        if (files == null || files.length == 0) {
            throw new IllegalStateException("Input directory is empty");
        }
        if (files.length > 1) {
            throw new IllegalStateException("More than one file in the input directory");
        }
        return files[0];
    }
    
    private static String convertFromTxt(File file) throws IOException {
        Pattern spacePattern = Pattern.compile(" ");
        return Files.readAllLines(file.toPath()).stream()
                .map(line -> spacePattern.splitAsStream(line).filter(s1 -> !s1.isBlank()).collect(Collectors.joining(",")))
                .collect(Collectors.joining(System.lineSeparator()));
    }
    
    private static String getName(File file) {
        Matcher matcher = fileNamePattern.matcher(file.getName());
        if (matcher.find()) {
            return matcher.group("name");
        } else {
            return "";
        }
    }
    
    private static String getExt(File file) {
        Matcher matcher = fileNamePattern.matcher(file.getName());
        if (matcher.find()) {
            return matcher.group("ext");
        } else {
            return "";
        }
    }
}
