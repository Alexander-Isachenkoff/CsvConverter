package ru.isachenkoff;

import org.apache.commons.lang3.StringUtils;
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
import java.text.SimpleDateFormat;
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
    
    private String processSheet(Sheet sheet) {
        return StreamSupport.stream(sheet.spliterator(), false)
                .map(this::processRow)
                .collect(Collectors.joining(System.lineSeparator()));
    }
    
    private String processRow(Row row) {
        return StreamSupport.stream(row.spliterator(), false)
                .map(this::processCell)
                .collect(Collectors.joining(getSeparator()));
    }
    
    private String processCell(Cell cell) {
        CellType cellType = cell.getCellType();
        String cellValue = String.valueOf(
                switch (cellType) {
                    case NUMERIC -> processCellWithNumericValue(cell);
                    case STRING -> cell.getStringCellValue();
                    case FORMULA -> processFormulaCell(cell, cell.getCachedFormulaResultType());
                    case BOOLEAN -> cell.getBooleanCellValue();
                    default -> "";
                });
        return quoteIfContainsSeparator(cellValue);
    }
    
    private static String processFormulaCell(Cell cell, CellType formulaResultType) {
        return String.valueOf(
                switch (formulaResultType) {
                    case NUMERIC -> processCellWithNumericValue(cell);
                    case STRING -> cell.getStringCellValue();
                    case BOOLEAN -> cell.getBooleanCellValue();
                    default -> "";
                });
    }
    
    private static String processCellWithNumericValue(Cell cell) {
        String formatString = cell.getCellStyle().getDataFormatString();
        boolean isData = StringUtils.containsAnyIgnoreCase(formatString, "m", "d", "s", "h", "y");
        if (isData) {
            return new SimpleDateFormat(formatString.replace("m", "M")).format(cell.getDateCellValue());
        } else {
            return processNumericValue(cell.getNumericCellValue());
        }
    }
    
    private static String processNumericValue(double nValue) {
        if (nValue == (int)nValue) {
            return String.valueOf((int)nValue);
        } else {
            return String.valueOf(nValue);
        }
    }
}
