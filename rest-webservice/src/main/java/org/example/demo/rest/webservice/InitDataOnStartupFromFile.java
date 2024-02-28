package org.example.demo.rest.webservice;

import org.example.demo.file.io.dao.FileRepository;
import org.example.demo.file.io.dto.CountryDTO;
import org.example.demo.rest.webservice.model.Country;
import org.example.demo.rest.webservice.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@ConditionalOnProperty(name = "data.source.isFile", havingValue = "true")
public class InitDataOnStartupFromFile implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(InitDataOnStartupFromFile.class);
    private final FileRepository<CountryDTO> fileRepository;
    private final CountryRepository countryRepository;

    public InitDataOnStartupFromFile(FileRepository<CountryDTO> fileRepository, CountryRepository countryRepository) {
        this.fileRepository = fileRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing data from file has started...");
        List<Country> countryList = fileRepository.readFile().stream().map(this::convert).toList();
        log.info(String.valueOf(countryList.size()));
        countryRepository.deleteAll();
        countryRepository.saveAll(countryList);
        log.info("...data initializing has finished successfully!");
    }

    private Country convert(CountryDTO countryDTO) {
        return new Country(
                null,
                countryDTO.countryName(),
                countryDTO.countryCode(),
                countryDTO.year(),
                countryDTO.value()
        );
    }
}
