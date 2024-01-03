package com.zerobase.reservation.domain.reservation.converter;

import com.zerobase.reservation.domain.reservation.type.ReservationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ReservationTypeConverter implements AttributeConverter<ReservationType, String> {
    @Override
    public String convertToDatabaseColumn(ReservationType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getDescription();
    }

    @Override
    public ReservationType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return ReservationType.fromDescription(dbData);
    }
}
