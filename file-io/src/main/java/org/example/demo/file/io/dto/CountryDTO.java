package org.example.demo.file.io.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Year;

@Builder
public record CountryDTO(String countryName, String countryCode, Year year, BigDecimal value) {
}
