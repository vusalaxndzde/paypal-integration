package com.vusalaxndzde.paypalintegration.service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.vusalaxndzde.paypalintegration.dto.PaymentOrder;
import com.vusalaxndzde.paypalintegration.dto.enumeration.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaypalService
{
    private final PayPalHttpClient payPalHttpClient;

    public PaymentOrder createPayment(BigDecimal fee)
    {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        AmountWithBreakdown amountBreakdown     = new AmountWithBreakdown().currencyCode("USD").value(fee.toString());
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().amountWithBreakdown(amountBreakdown);
        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl("https://google.com")
                .cancelUrl("https://google.com");
        orderRequest.applicationContext(applicationContext);

        OrdersCreateRequest ordersCreateRequest = new OrdersCreateRequest().requestBody(orderRequest);

        try
        {
            HttpResponse<Order> orderHttpResponse = payPalHttpClient.execute(ordersCreateRequest);
            Order               order             = orderHttpResponse.result();

            String redirectUrl = order.links().stream()
                    .filter(link -> "approve".equals(link.rel()))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new)
                    .href();

            return PaymentOrder.builder()
                    .status(PaymentStatus.SUCCESS)
                    .payId(order.id())
                    .redirectUrl(redirectUrl)
                    .build();
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }

        return PaymentOrder.builder().status(PaymentStatus.ERROR).build();
    }

    public PaymentOrder completePayment(String token)
    {
        OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(token);
        try
        {
            HttpResponse<Order> httpResponse = payPalHttpClient.execute(ordersCaptureRequest);
            if (httpResponse.result().status() != null)
            {
                return PaymentOrder.builder()
                        .status(PaymentStatus.SUCCESS)
                        .payId(token)
                        .build();
            }
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }

        return PaymentOrder.builder().status(PaymentStatus.ERROR).build();
    }
}
