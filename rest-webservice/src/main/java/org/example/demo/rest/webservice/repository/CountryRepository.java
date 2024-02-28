package org.example.demo.rest.webservice.repository;

import org.example.demo.rest.webservice.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>, PagingAndSortingRepository<Country, Integer> {

    List<Country> findByCountryCodeOrderByYear(String countryCode);

    List<Country> findByYearOrderByCountryName(Year year);
}
