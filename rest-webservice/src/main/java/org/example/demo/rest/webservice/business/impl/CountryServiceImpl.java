package org.example.demo.rest.webservice.business.impl;

import org.example.demo.rest.webservice.exceptions.ResourceNotFoundException;
import org.example.demo.rest.webservice.model.Country;
import org.example.demo.rest.webservice.business.CountryService;
import org.example.demo.rest.webservice.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Year;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService<Country> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Page<Country> findAll(int page, int size) {
        LOGGER.info("Querying for countries with page {}, size {}", page, size);
        Page<Country> countryPage = countryRepository.findAll(PageRequest.of(page, size));
        if (countryPage.isEmpty()) {
            throw new ResourceNotFoundException("No data to display");
        }
        return countryPage;
    }

    @Override
    public List<Country> findByCountryCode(String code) {
        LOGGER.info("Querying for countries with countryCode {}", code);
        List<Country> countryList = countryRepository.findByCountryCodeOrderByYear(code);
        if (countryList.isEmpty()) {
            throw new ResourceNotFoundException("There are no data to display with code: " + code);
        }
        return countryList;
    }

    @Override
    public List<Country> findByYear(Year year) {
        LOGGER.info("Querying for countries with year {}", year);
        List<Country> countryList = countryRepository.findByYearOrderByCountryName(year);
        if (countryList.isEmpty()) {
            throw new ResourceNotFoundException("There are no data to display with year: " + year);
        }
        return countryList;
    }
}
