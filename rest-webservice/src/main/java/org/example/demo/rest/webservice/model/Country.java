package org.example.demo.rest.webservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.demo.rest.webservice.model.converter.YearAttributeConverter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Year;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String countryName;
    private String countryCode;
    @Convert(converter = YearAttributeConverter.class)
    private Year year;
    private BigDecimal value;


}
