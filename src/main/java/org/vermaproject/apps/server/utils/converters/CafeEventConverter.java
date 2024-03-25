package org.vermaproject.apps.server.utils.converters;

import org.vermaproject.apps.server.enums.CafeEventEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public final class CafeEventConverter implements AttributeConverter<CafeEventEnum, String> {
    @Override
    public String convertToDatabaseColumn(CafeEventEnum cafeEventEnum) {
        if (cafeEventEnum == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return cafeEventEnum.toString();
    }

    @Override
    public CafeEventEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return CafeEventEnum.valueOf(s);
    }
}
