package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.CafeState;

@Converter
public final class CafeStateConverter implements AttributeConverter<CafeState, String> {
    @Override
    public String convertToDatabaseColumn(CafeState cafeState) {
        if (cafeState == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return cafeState.toString();
    }

    @Override
    public CafeState convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return CafeState.valueOf(s);
    }
}
