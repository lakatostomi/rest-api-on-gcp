package org.example.demo.file.io.dao.impl;

import org.example.demo.file.io.dao.FileRepository;
import org.example.demo.file.io.dto.CountryDTO;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "file.source.isJson", havingValue = "false", matchIfMissing = true)
public class FileRepositoryXMLImpl implements FileRepository<CountryDTO> {

    @Value("${file.source.location}")
    private String fileSource;

    @Override
    public List<CountryDTO> readFile() throws Exception {
        SAXBuilder builder = new SAXBuilder();
        builder.build(new URI(fileSource).toURL());
        Document doc = builder.build(fileSource);
        Element rootElement = doc.getRootElement();
        List<Element> childElements = rootElement.getChildren("country");
        return childElements.stream().map(child-> CountryDTO.builder()
                .countryName(child.getChildText("countryName"))
                .countryCode(child.getChildText("countryCode"))
                .year(Year.parse(child.getChildText("year")))
                .value(new BigDecimal(child.getChildText("value")))
                .build()).collect(Collectors.toList());
    }
}
