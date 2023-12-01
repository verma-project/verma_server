package io.github.shymega.repaircafe.api.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Converter
public final class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = "::";

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        return stringList != null ? String.join(SPLIT_CHAR, stringList) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return s != null ? Arrays.asList(s.split(SPLIT_CHAR)) : Collections.emptyList();
    }
}
