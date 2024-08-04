package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.PatTestResult;

@Converter
public final class PatTestResultConverter implements AttributeConverter<PatTestResult, String> {
    @Override
    public String convertToDatabaseColumn(PatTestResult patTestResult) {
        if (patTestResult == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return patTestResult.toString();
    }

    @Override
    public PatTestResult convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return PatTestResult.valueOf(s);
    }
}
