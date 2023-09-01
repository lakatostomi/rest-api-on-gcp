package org.example.demo.rest.webservice;

import org.example.demo.file.io.dao.FileRepository;
import org.example.demo.file.io.dto.CountryDTO;
import org.example.demo.rest.webservice.model.Country;
import org.example.demo.rest.webservice.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.List;

@Component
public class InitDataOnStartup implements CommandLineRunner, Converter<CountryDTO, Country> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitDataOnStartup.class);
    private final FileRepository<CountryDTO> fileRepository;
    private final CountryRepository countryRepository;

    public InitDataOnStartup(FileRepository<CountryDTO> fileRepository, CountryRepository countryRepository) {
        this.fileRepository = fileRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Initializing data...");
        List<Country> countryList = fileRepository.readFile().stream()
                .map(this::convert).toList();
        countryRepository.saveAllAndFlush(countryList);
        LOGGER.info("...has finished successfully");
    }

    @Override
    public Country convert(CountryDTO source) {
        return new Country(null, source.countryName(), source.countryCode(), Year.of(source.year().getValue()), source.value());
    }
}
