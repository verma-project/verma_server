package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.TicketStateEventEnum;

@Converter
public final class TicketStateEventConverter implements AttributeConverter<TicketStateEventEnum, String> {

    @Override
    public String convertToDatabaseColumn(TicketStateEventEnum ticketStateEventEnum) {
        if (ticketStateEventEnum == null)
            throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return ticketStateEventEnum.toString();
    }

    @Override
    public TicketStateEventEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return TicketStateEventEnum.valueOf(s);
    }
}
