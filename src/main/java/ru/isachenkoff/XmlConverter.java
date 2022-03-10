package ru.isachenkoff;

import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class XmlConverter extends AbstractConverter {
    
    @Override
    public List<Pair<String, String>> convert(File file) throws IOException {
        Node root;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new FileInputStream(file));
            root = document.getDocumentElement();
        } catch (ParserConfigurationException | SAXException ex) {
            ex.printStackTrace();
            return null;
        }
        
        List<Map<String, String>> table = new ArrayList<>();
        elements(root).forEach(xmlRow -> {
            Map<String, String> row = new LinkedHashMap<>();
            table.add(row);
            elements(xmlRow).forEach(xmlColumn -> row.put(xmlColumn.getNodeName(), xmlColumn.getChildNodes().item(0).getTextContent()));
        });
        Set<String> columnNames = new LinkedHashSet<>();
        for (Map<String, String> map : table) {
            columnNames.addAll(map.keySet());
        }
        String tableContent = table.stream().map(row -> columnNames.stream()
                        .map(colName -> quoteIfContainsSeparator(row.getOrDefault(colName, "")))
                        .collect(Collectors.joining(getSeparator()))
                )
                .collect(Collectors.joining(System.lineSeparator()));
        String result = String.join(getSeparator(), columnNames) + System.lineSeparator() + tableContent;
        return List.of(Pair.of(FileUtils.getName(file), result));
    }
    
    private static Stream<Node> elements(Node node) {
        return asStream(node.getChildNodes())
                .filter(el -> el.getNodeType() != Node.TEXT_NODE);
    }
    
    private static Stream<Node> asStream(NodeList nodeList) {
        return IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item);
    }
    
}
