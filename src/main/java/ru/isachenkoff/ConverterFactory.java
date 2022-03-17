package ru.isachenkoff;

import java.io.File;
import java.util.HashMap;
import java.util.function.Supplier;

public class ConverterFactory {
    
    private static final HashMap<String, Supplier<AbstractConverter>> fileTypeConverterMap = new HashMap<>();
    
    static {
        fileTypeConverterMap.put("txt", TxtConverter::new);
        fileTypeConverterMap.put("xlsx", XlsxConverter::new);
        fileTypeConverterMap.put("docx", DocxConverter::new);
        fileTypeConverterMap.put("pdf", PdfConverter::new);
        fileTypeConverterMap.put("xml", XmlConverter::new);
    }
    
    static AbstractConverter findConverterFor(File file) {
        String ext = FileUtils.getExt(file).toLowerCase();
        if (!fileTypeConverterMap.containsKey(ext)) {
            throw new RuntimeException("Unexpected file type");
        }
        return fileTypeConverterMap.get(ext).get();
    }
}
