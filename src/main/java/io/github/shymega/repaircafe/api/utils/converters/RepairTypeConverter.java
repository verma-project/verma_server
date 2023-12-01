package io.github.shymega.repaircafe.api.utils.converters;

import io.github.shymega.repaircafe.api.enums.RepairTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;

@Converter
public final class RepairTypeConverter implements AttributeConverter<RepairTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(RepairTypeEnum repairTypeEnum) {
        if (repairTypeEnum == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return repairTypeEnum.toString();
    }

    @Override
    public RepairTypeEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return RepairTypeEnum.valueOf(s);
    }
}
