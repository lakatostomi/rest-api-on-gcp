package org.example.demo.rest.webservice.api;


import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.rest.webservice.model.Country;
import org.example.demo.rest.webservice.business.CountryService;
import org.example.demo.rest.webservice.pagination.PaginationDiscoverabilityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/rest/v1/countries")
public class CountryController {

    private static final Logger log = LoggerFactory.getLogger(CountryController.class);
    private final CountryService<Country> countryService;
    private final ApplicationEventPublisher eventPublisher;

    public CountryController(CountryService countryService, ApplicationEventPublisher eventPublisher) {
        this.countryService = countryService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public ResponseEntity<List<Country>> findAll(@RequestParam int page, UriComponentsBuilder uriComponentsBuilder,
                                                 HttpServletResponse httpServletResponse) {
        Page<Country> countryPage = countryService.findAll(page, 100);

        eventPublisher.publishEvent(new PaginationDiscoverabilityEvent<>(Country.class,
                uriComponentsBuilder, httpServletResponse, page, countryPage.getTotalPages()));

        loggingResult(countryPage.getContent());
        return ResponseEntity.ok(countryPage.getContent());
    }

    @GetMapping("/code")
    public ResponseEntity<List<Country>> findByCountryCode(@RequestParam String code) {
        List<Country> countryList = countryService.findByCountryCode(code);
        loggingResult(countryList);
        return ResponseEntity.ok(countryList);
    }

    @GetMapping("/year")
    public ResponseEntity<List<Country>> findByYear(@RequestParam int year) {
        List<Country> countryList = countryService.findByYear(Year.of(year));
        loggingResult(countryList);
        return ResponseEntity.ok(countryList);
    }

    private void loggingResult(List<Country> countryList) {
        log.info("Returning a country list with size={}", countryList.size());
    }
}
