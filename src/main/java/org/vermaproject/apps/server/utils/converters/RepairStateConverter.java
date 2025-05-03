// SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
//
// SPDX-License-Identifier: AGPL-3.0-only

package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.RepairState;

@Converter
public final class RepairStateConverter implements AttributeConverter<RepairState, String> {

    @Override
    public String convertToDatabaseColumn(RepairState repairEvent) {
        if (repairEvent == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return repairEvent.toString();
    }

    @Override
    public RepairState convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return RepairState.valueOf(s);
    }
}
