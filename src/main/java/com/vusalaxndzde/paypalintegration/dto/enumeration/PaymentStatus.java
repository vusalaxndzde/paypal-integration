package com.vusalaxndzde.paypalintegration.dto.enumeration;

import lombok.Getter;

@Getter
public enum PaymentStatus
{
    SUCCESS(1),
    ERROR(2);

    private final Integer id;

    PaymentStatus(Integer id)
    {
        this.id = id;
    }
}
