package org.example.demo.rest.webservice.model.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;


import java.time.Year;
@Component
public class YearAttributeConverter implements AttributeConverter<Year, Short> {


    @Override
    public Short convertToDatabaseColumn(Year year) {
        if (year != null) {
            return (short) year.getValue();
        }
        return null;
    }

    @Override
    public Year convertToEntityAttribute(Short attribute) {
        if (attribute != null) {
            return Year.of(attribute);
        }
        return null;
    }
}
