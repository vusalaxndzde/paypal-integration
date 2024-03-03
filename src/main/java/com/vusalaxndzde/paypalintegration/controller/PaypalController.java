package com.vusalaxndzde.paypalintegration.controller;

import com.vusalaxndzde.paypalintegration.dto.PaymentOrder;
import com.vusalaxndzde.paypalintegration.service.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/paypal")
@RequiredArgsConstructor
public class PaypalController
{
    private final PaypalService service;

    @PostMapping("/init")
    public PaymentOrder createPayment(@RequestParam("sum") BigDecimal sum)
    {
        return service.createPayment(sum);
    }

    @PostMapping("/capture")
    public PaymentOrder completePayment(@RequestParam("token") String token)
    {
        return service.completePayment(token);
    }
}
