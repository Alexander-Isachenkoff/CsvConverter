package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class AbstractConverter {
    
    private final String separator = ",";
    
    public abstract List<Pair<String, String>> convert(File file) throws IOException;
    
    public String getSeparator() {
        return separator;
    }
    
    String quoteIfContainsSeparator(String string) {
        return string.contains(separator) ? quote(string) : string;
    }
    
    private static String quote(String string) {
        return "\"" + string + "\"";
    }
}
