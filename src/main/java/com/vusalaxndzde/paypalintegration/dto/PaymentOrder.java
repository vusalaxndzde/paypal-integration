package com.vusalaxndzde.paypalintegration.dto;

import com.vusalaxndzde.paypalintegration.dto.enumeration.PaymentStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrder
{

    private PaymentStatus status;

    private String payId;

    private String redirectUrl;

}
