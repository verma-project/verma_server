// SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
//
// SPDX-License-Identifier: AGPL-3.0-only

package org.vermaproject.apps.server.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.vermaproject.apps.server.enums.RepairType;

@Converter
public final class RepairTypeConverter implements AttributeConverter<RepairType, String> {
    @Override
    public String convertToDatabaseColumn(RepairType repairType) {
        if (repairType == null) throw new IllegalArgumentException("DB error. Enum variant is not populated.");

        return repairType.toString();
    }

    @Override
    public RepairType convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("DB error. Value (String) is not populated.");

        return RepairType.valueOf(s);
    }
}
