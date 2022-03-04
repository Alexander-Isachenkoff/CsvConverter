package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class AbstractConverter {
    public abstract List<Pair<String, String>> convert(File file) throws IOException;
}
