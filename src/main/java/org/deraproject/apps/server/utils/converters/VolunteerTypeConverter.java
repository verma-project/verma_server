package org.deraproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.deraproject.apps.server.db.entities.Volunteer;
import org.deraproject.apps.server.enums.RepairTypeEnum;
import org.deraproject.apps.server.enums.VolunteerTypeEnum;

@Converter
public final class VolunteerTypeConverter implements AttributeConverter<VolunteerTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(VolunteerTypeEnum volunteerTypeEnum) {
        if (volunteerTypeEnum == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return volunteerTypeEnum.toString();
    }

    @Override
    public VolunteerTypeEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return VolunteerTypeEnum.valueOf(s);
    }
}
