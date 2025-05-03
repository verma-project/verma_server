// SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
//
// SPDX-License-Identifier: AGPL-3.0-only

package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.PatTestState;

@Converter
public final class PatTestStateConverter implements AttributeConverter<PatTestState, String> {
    @Override
    public String convertToDatabaseColumn(PatTestState patTestState) {
        if (patTestState == null)
            throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return patTestState.toString();
    }

    @Override
    public PatTestState convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return PatTestState.valueOf(s);
    }
}
