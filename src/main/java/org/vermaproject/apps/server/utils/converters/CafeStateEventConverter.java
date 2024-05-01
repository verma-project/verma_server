package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.CafeStateEventEnum;

@Converter
public final class CafeStateEventConverter implements AttributeConverter<CafeStateEventEnum, String> {
    @Override
    public String convertToDatabaseColumn(CafeStateEventEnum cafeStateEventEnum) {
        if (cafeStateEventEnum == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return cafeStateEventEnum.toString();
    }

    @Override
    public CafeStateEventEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return CafeStateEventEnum.valueOf(s);
    }
}
