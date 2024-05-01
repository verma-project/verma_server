package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.RepairStateEventEnum;

@Converter
public final class RepairStateEventConverter implements AttributeConverter<RepairStateEventEnum, String> {

    @Override
    public String convertToDatabaseColumn(RepairStateEventEnum repairEvent) {
        if (repairEvent == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return repairEvent.toString();
    }

    @Override
    public RepairStateEventEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return RepairStateEventEnum.valueOf(s);
    }
}
