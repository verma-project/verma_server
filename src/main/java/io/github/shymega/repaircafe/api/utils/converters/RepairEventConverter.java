package io.github.shymega.repaircafe.api.utils.converters;

import io.github.shymega.repaircafe.api.enums.RepairEventEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RepairEventConverter implements AttributeConverter<RepairEventEnum, String> {

    @Override
    public String convertToDatabaseColumn(RepairEventEnum repairEvent) {
        if (repairEvent == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return repairEvent.toString();
    }

    @Override
    public RepairEventEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return RepairEventEnum.valueOf(s);
    }
}
