package org.example.demo.file.io.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.demo.file.io.dao.FileRepository;
import org.example.demo.file.io.dto.CountryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;


@Component
@ConditionalOnProperty(name = "file.source.isJson", havingValue = "true")
public class FileRepositoryJSONImpl implements FileRepository<CountryDTO> {

    @Value("${file.source.location}")
    private String fileSource;

    private ObjectMapper objectMapper;

    public FileRepositoryJSONImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<CountryDTO> readFile() throws Exception{
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(new URL(fileSource), new TypeReference<List<CountryDTO>>() {});
    }

}
