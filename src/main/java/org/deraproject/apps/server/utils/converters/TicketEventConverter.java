package org.deraproject.apps.server.utils.converters;

import org.deraproject.apps.server.enums.TicketEventEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public final class TicketEventConverter implements AttributeConverter<TicketEventEnum, String> {

    @Override
    public String convertToDatabaseColumn(TicketEventEnum ticketEventEnum) {
        if (ticketEventEnum == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return ticketEventEnum.toString();
    }

    @Override
    public TicketEventEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return TicketEventEnum.valueOf(s);
    }
}
