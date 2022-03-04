package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class Converter {
    
    public static void main(String[] args) throws IOException {
        File inputFile = FileUtils.getInputFile();
        AbstractConverter converter = findConverterFor(inputFile);
        List<Pair<String, String>> result = converter.convert(inputFile);
        for (Pair<String, String> pair : result) {
            FileUtils.writeCsv(pair.getLeft(), pair.getRight());
        }
    }
    
    private static AbstractConverter findConverterFor(File file) {
        String ext = FileUtils.getExt(file).toLowerCase();
        HashMap<String, Supplier<AbstractConverter>> map = new HashMap<>();
        map.put("txt", TxtConverter::new);
        map.put("xlsx", XlsxConverter::new);
        if (!map.containsKey(ext)) {
            throw new RuntimeException("Unexpected file type");
        }
        return map.get(ext).get();
    }
    
}
