package io.github.shymega.repaircafe.api.utils.converters;

import io.github.shymega.repaircafe.api.enums.TicketEventEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TicketEventConverter implements AttributeConverter<TicketEventEnum, String> {

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
