package ru.isachenkoff;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TxtConverter {
    
    public static String convert(File file) throws IOException {
        Pattern spacePattern = Pattern.compile(" ");
        return Files.readAllLines(file.toPath()).stream()
                .map(line -> spacePattern.splitAsStream(line).filter(s1 -> !s1.isBlank()).collect(Collectors.joining(",")))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
