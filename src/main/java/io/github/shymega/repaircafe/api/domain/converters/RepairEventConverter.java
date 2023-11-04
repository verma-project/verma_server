package io.github.shymega.repaircafe.api.domain.converters;

import io.github.shymega.repaircafe.api.domain.enums.RepairEvent;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;

@Converter
public class RepairEventConverter implements AttributeConverter<RepairEvent, String> {

    @Override
    public String convertToDatabaseColumn(RepairEvent repairEvent) {
        if (repairEvent == null) return null;

        return repairEvent.toString();
    }

    @Override
    public RepairEvent convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) return null;

        return RepairEvent.valueOf(s);
    }
}
