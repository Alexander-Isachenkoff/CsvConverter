package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class XlsxConverter extends AbstractConverter {
    @Override
    public List<Pair<String, String>> convert(File file) throws IOException {
        InputStream fs = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fs);
        return StreamSupport.stream(wb.spliterator(), false)
                .map(sheet -> Pair.of(sheet.getSheetName(), processSheet(sheet)))
                .collect(Collectors.toList());
    }
    
    private static String processSheet(Sheet sheet) {
        return StreamSupport.stream(sheet.spliterator(), false)
                .map(XlsxConverter::processRow)
                .collect(Collectors.joining(System.lineSeparator()));
    }
    
    private static String processRow(Row row) {
        return StreamSupport.stream(row.spliterator(), false)
                .map(XlsxConverter::processCell)
                .collect(Collectors.joining(","));
    }
    
    private static String processCell(Cell cell) {
        CellType cellType = cell.getCellType();
        String cellValue = String.valueOf(
                switch (cellType) {
                    case NUMERIC -> cell.getNumericCellValue();
                    case STRING -> cell.getStringCellValue();
                    case FORMULA -> processFormulaCell(cell, cell.getCachedFormulaResultType());
                    case BOOLEAN -> cell.getBooleanCellValue();
                    default -> "";
                });
        return cellValue.contains(",") ? quote(cellValue) : cellValue;
    }
    
    private static String processFormulaCell(Cell cell, CellType formulaResultType) {
        String cellValue = String.valueOf(
                switch (formulaResultType) {
                    case NUMERIC -> cell.getNumericCellValue();
                    case STRING -> cell.getStringCellValue();
                    case BOOLEAN -> cell.getBooleanCellValue();
                    default -> "";
                });
        return cellValue.contains(",") ? quote(cellValue) : cellValue;
    }
    
    private static String quote(String string) {
        return "\"" + string + "\"";
    }
}
