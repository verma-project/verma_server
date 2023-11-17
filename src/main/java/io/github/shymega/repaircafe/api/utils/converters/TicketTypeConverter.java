package io.github.shymega.repaircafe.api.utils.converters;

import io.github.shymega.repaircafe.api.enums.TicketTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TicketTypeConverter implements AttributeConverter<TicketTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(TicketTypeEnum ticketTypeEnum) {
        if (ticketTypeEnum == null) throw new IllegalArgumentException("error"); // FIXME.

        return ticketTypeEnum.toString();
    }

    @Override
    public TicketTypeEnum convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("error"); // FIXME.

        return TicketTypeEnum.valueOf(s);
    }
}
