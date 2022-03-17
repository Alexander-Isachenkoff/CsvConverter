package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Converter {
    
    public static void main(String[] args) {
        for (File file : FileUtils.getInputFiles()) {
            System.out.println("Processing " + file.getName());
            processFile(file);
        }
    }
    
    private static void processFile(File inputFile) {
        AbstractConverter converter = ConverterFactory.findConverterFor(inputFile);
        List<Pair<String, String>> result;
        try {
            result = converter.convert(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for (Pair<String, String> pair : result) {
            FileUtils.writeCsv(pair.getLeft(), pair.getRight());
        }
    }
    
}
