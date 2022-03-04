package ru.isachenkoff;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Converter {
    
    public static void main(String[] args) {
        File inputFile = FileUtils.getInputFile();
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
        FileUtils.writeCsv(FileUtils.getName(inputFile), converted);
    }
    
    private static String convertFromTxt(File file) throws IOException {
        Pattern spacePattern = Pattern.compile(" ");
        return Files.readAllLines(file.toPath()).stream()
                .map(line -> spacePattern.splitAsStream(line).filter(s1 -> !s1.isBlank()).collect(Collectors.joining(",")))
                .collect(Collectors.joining(System.lineSeparator()));
    }
    
}
