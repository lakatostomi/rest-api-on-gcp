package org.example.demo.rest.webservice;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import org.example.demo.rest.webservice.model.Country;
import org.example.demo.rest.webservice.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "data.source.isFile", havingValue = "false", matchIfMissing = true)
public class InitDataOnStartupFromBigQuery implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitDataOnStartupFromBigQuery.class);
    private final CountryRepository countryRepository;
    private final BigQuery bigQuery;
    @Value( "${spring.cloud.gcp.bigquery.datasetName}")
    private String dataset;

    public InitDataOnStartupFromBigQuery(CountryRepository countryRepository, BigQuery bigQuery) {
        this.countryRepository = countryRepository;
        this.bigQuery = bigQuery;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Initializing data has started...");
        countryRepository.saveAll(readDataSet());
        LOGGER.info("...data initializing has finished successfully!");
    }

    public List<Country> readDataSet() throws InterruptedException {
        String query = "SELECT * FROM " + dataset;
        QueryJobConfiguration queryJobConfiguration = QueryJobConfiguration.newBuilder(query).build();
        return convert(bigQuery.query(queryJobConfiguration).iterateAll());
    }


    public List<Country> convert(Iterable<FieldValueList> source) {
        List<Country> countryList = new ArrayList<>();
        for (FieldValueList fieldValueList : source) {
            Country country = Country.builder()
                    .countryName(fieldValueList.get(3).getStringValue())
                    .countryCode(fieldValueList.get(2).getStringValue())
                    .year(Year.of(Integer.parseInt(fieldValueList.get(1).getStringValue())))
                    .value(new BigDecimal(fieldValueList.get(0).getStringValue()))
                    .build();
            countryList.add(country);
        }
        return countryList;
    }
}

