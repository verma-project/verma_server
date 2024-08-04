package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.VolunteerType;

@Converter
public final class VolunteerTypeConverter implements AttributeConverter<VolunteerType, String> {
    @Override
    public String convertToDatabaseColumn(VolunteerType volunteerType) {
        if (volunteerType == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return volunteerType.toString();
    }

    @Override
    public VolunteerType convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return VolunteerType.valueOf(s);
    }
}
