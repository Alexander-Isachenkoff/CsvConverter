package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class Converter {
    
    private static final HashMap<String, Supplier<AbstractConverter>> fileTypeConverterMap = new HashMap<>();
    
    static {
        fileTypeConverterMap.put("txt", TxtConverter::new);
        fileTypeConverterMap.put("xlsx", XlsxConverter::new);
        fileTypeConverterMap.put("docx", DocxConverter::new);
        fileTypeConverterMap.put("pdf", PdfConverter::new);
    }
    
    public static void main(String[] args) {
        for (File file : FileUtils.getInputFiles()) {
            System.out.println("Processing " + file.getName());
            processFile(file);
        }
    }
    
    private static void processFile(File inputFile) {
        AbstractConverter converter = findConverterFor(inputFile);
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
    
    private static AbstractConverter findConverterFor(File file) {
        String ext = FileUtils.getExt(file).toLowerCase();
        if (!fileTypeConverterMap.containsKey(ext)) {
            throw new RuntimeException("Unexpected file type");
        }
        return fileTypeConverterMap.get(ext).get();
    }
    
}
