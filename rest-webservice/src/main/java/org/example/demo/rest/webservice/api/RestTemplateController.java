package org.example.demo.rest.webservice.api;

import org.example.demo.rest.webservice.InitDataOnStartupFromFile;
import org.example.demo.rest.webservice.business.CountryService;
import org.example.demo.rest.webservice.business.impl.CountryServiceImpl;
import org.example.demo.rest.webservice.dto.ElementAttributeDTO;
import org.example.demo.rest.webservice.dto.HtmlElementDTO;
import org.example.demo.rest.webservice.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/rest/v1/countries/generate/html")
public class RestTemplateController {

    private static final Logger log = LoggerFactory.getLogger(RestTemplateController.class);
    private final CountryService<Country> countryService;
    private final RestTemplate restTemplate;

    @Value("${html.service.url}")
    private String url;

    public RestTemplateController(CountryServiceImpl countryService, RestTemplateBuilder builder) {
        this.countryService = countryService;
        this.restTemplate = builder.build();
    }

    @GetMapping
    public ResponseEntity<String> getHtmlCode(@RequestParam String code) {
        String baseUrl = url +"/api/rest/v1/codes";
        List<Country> countryList = countryService.findByCountryCode(code);

        CompletableFuture<String> requestFuture = CompletableFuture.supplyAsync(() -> {
            List<HtmlElementDTO> htmlElementDTOList = createElements();
            for (int i = 0; i < countryList.size(); i++) {
                HtmlElementDTO trData = new HtmlElementDTO(2, "tr", "", new ElementAttributeDTO("", ""));
                htmlElementDTOList.add(trData);
            }

            for (int i = 0; i < countryList.size(); i++) {
                HtmlElementDTO tdCName = new HtmlElementDTO(i+8, "td", countryList.get(i).getCountryName(), new ElementAttributeDTO("", ""));
                HtmlElementDTO tdCode = new HtmlElementDTO(i+8, "td", countryList.get(i).getCountryCode(), new ElementAttributeDTO("", ""));
                HtmlElementDTO tdyear = new HtmlElementDTO(i+8, "td", String.valueOf(countryList.get(i).getYear()), new ElementAttributeDTO("", ""));
                HtmlElementDTO tdpopulation = new HtmlElementDTO(i+8, "td", countryList.get(i).getValue().toString(), new ElementAttributeDTO("", ""));
                htmlElementDTOList.addAll(List.of(tdCName, tdCode, tdyear, tdpopulation));
            }
            log.info("Sending requests to url: " + baseUrl);
            for (HtmlElementDTO htmlElementDTO : htmlElementDTOList) {
                restTemplate.postForEntity(baseUrl, htmlElementDTO, List.class);
            }
            log.info("Requests have been sent successfully!");

            String httpResponse = restTemplate.getForObject(baseUrl + "/save", String.class);
            log.info("Saving finished response created!");
            return httpResponse;

        }).exceptionally(ex -> "<html><body>Server error: " + ex.getMessage() + "</body></html>");

        String result = requestFuture.join();

        return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
    }

    private List<HtmlElementDTO> createElements() {
        HtmlElementDTO h1 = new HtmlElementDTO(0, "h1", "Population Data of the country from 1960 - 2010", new ElementAttributeDTO("class", "text-center"));
        HtmlElementDTO table = new HtmlElementDTO(0, "table", "", new ElementAttributeDTO("class", "table table-hover table-bordered text-center table-success"));
        HtmlElementDTO tr = new HtmlElementDTO(2, "tr", "", new ElementAttributeDTO("class", "table-secondary"));
        HtmlElementDTO tdCountryName = new HtmlElementDTO(3, "td", "Country Name", new ElementAttributeDTO("", ""));
        HtmlElementDTO tdCountryCode = new HtmlElementDTO(3, "td", "Country Code", new ElementAttributeDTO("", ""));
        HtmlElementDTO tdYear = new HtmlElementDTO(3, "td", "Year", new ElementAttributeDTO("", ""));
        HtmlElementDTO tdPopulation = new HtmlElementDTO(3, "td", "Population", new ElementAttributeDTO("", ""));
        return new ArrayList<>(List.of(h1, table, tr, tdCountryName, tdCountryCode, tdYear, tdPopulation));
    }
}
