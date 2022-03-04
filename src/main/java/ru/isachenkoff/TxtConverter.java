package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TxtConverter extends AbstractConverter {
    
    @Override
    public List<Pair<String, String>> convert(File file) throws IOException {
        Pattern spacePattern = Pattern.compile(" ");
        String result = Files.readAllLines(file.toPath()).stream()
                .map(line -> spacePattern.splitAsStream(line).filter(s1 -> !s1.isBlank()).collect(Collectors.joining(",")))
                .collect(Collectors.joining(System.lineSeparator()));
        return List.of(Pair.of(file.getName(), result));
    }
}
