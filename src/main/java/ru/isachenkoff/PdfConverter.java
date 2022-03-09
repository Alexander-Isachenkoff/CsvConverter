package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.List;

// TODO: Does not work yet
public class PdfConverter extends AbstractConverter {
    
    @Override
    public List<Pair<String, String>> convert(File file) throws IOException {
        String parsedText;
        PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
        parser.parse();
        COSDocument cosDoc = parser.getDocument();
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cosDoc);
        parsedText = pdfStripper.getText(pdDoc);
        return List.of(Pair.of(FileUtils.getName(file), parsedText));
    }
}
