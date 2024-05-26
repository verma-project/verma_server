package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.PatTestStateEventEnum;

@Converter
public final class PatTestStateEventConverter implements AttributeConverter<PatTestStateEventEnum, String> {
    @Override
    public String convertToDatabaseColumn(PatTestStateEventEnum patTestStateEventEnum) {
        if (patTestStateEventEnum == null)
            throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return patTestStateEventEnum.toString();
    }

    @Override
    public PatTestStateEventEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return PatTestStateEventEnum.valueOf(s);
    }
}
