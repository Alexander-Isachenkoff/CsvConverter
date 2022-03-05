package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class DocxConverter extends AbstractConverter {
    @Override
    public List<Pair<String, String>> convert(File file) throws IOException {
        InputStream fs = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(fs);
        XWPFTable tbl = document.getTables().get(0);
        String result = tbl.getRows().stream()
                .map(row -> row.getTableCells().stream()
                        .map(cell -> quoteIfContainsSeparator(cell.getText()))
                        .collect(Collectors.joining(getSeparator()))
                )
                .collect(Collectors.joining(System.lineSeparator()));
        return List.of(Pair.of(FileUtils.getName(file), result));
    }
}
