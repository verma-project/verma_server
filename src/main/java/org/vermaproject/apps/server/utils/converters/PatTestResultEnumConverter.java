package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.PatTestResultEnum;

@Converter
public final class PatTestResultEnumConverter implements AttributeConverter<PatTestResultEnum, String> {
    @Override
    public String convertToDatabaseColumn(PatTestResultEnum patTestResultEnum) {
        if (patTestResultEnum == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return patTestResultEnum.toString();
    }

    @Override
    public PatTestResultEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return PatTestResultEnum.valueOf(s);
    }
}
