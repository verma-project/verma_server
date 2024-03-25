package org.vermaproject.apps.server.utils.converters;

import org.vermaproject.apps.server.enums.TicketTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TicketTypeConverter implements AttributeConverter<TicketTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(TicketTypeEnum ticketTypeEnum) {
        if (ticketTypeEnum == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return ticketTypeEnum.toString();
    }

    @Override
    public TicketTypeEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return TicketTypeEnum.valueOf(s);
    }
}
