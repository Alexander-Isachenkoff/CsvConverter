package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DocxConverter extends AbstractConverter {
    @Override
    public List<Pair<String, String>> convert(File file) throws IOException {
        InputStream fs = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(fs);
        List<Pair<String, String>> results = new ArrayList<>();
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            XWPFTable table = tables.get(i);
            String result = processTable(table);
            results.add(Pair.of(FileUtils.getName(file) + i, result));
        }
        return results;
    }
    
    private String processTable(XWPFTable table) {
        return table.getRows().stream()
                .map(this::processRow)
                .collect(Collectors.joining(System.lineSeparator()));
    }
    
    private String processRow(XWPFTableRow row) {
        return row.getTableCells().stream()
                .map(cell -> quoteIfContainsSeparator(cell.getText()))
                .collect(Collectors.joining(getSeparator()));
    }
}
