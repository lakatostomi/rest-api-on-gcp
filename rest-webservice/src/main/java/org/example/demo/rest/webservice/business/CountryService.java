package org.example.demo.rest.webservice.business;

import org.example.demo.rest.webservice.model.Country;
import org.springframework.data.domain.Page;

import java.time.Year;
import java.util.List;

public interface CountryService<T extends Country> {

    Page<T> findAll(int page, int size);
    List<T> findByCountryCode(String code);
    List<T> findByYear(Year year);
}
