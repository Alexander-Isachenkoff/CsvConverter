package ru.isachenkoff;

import java.io.File;
import java.io.IOException;

public class Converter {
    
    public static void main(String[] args) {
        File inputFile = FileUtils.getInputFile();
        String converted;
        if (inputFile.getName().toLowerCase().endsWith(".txt")) {
            try {
                converted = TxtConverter.convert(inputFile);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            throw new RuntimeException("Unexpected file type");
        }
        FileUtils.writeCsv(FileUtils.getName(inputFile), converted);
    }
    
}
