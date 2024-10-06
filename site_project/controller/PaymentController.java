package com.ecommerce.site_project.controller;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.exception.StripeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {

	@Autowired
	public PaymentController(@Value("${stripe.apikey}") String stripeSecretKey) {
	    Stripe.apiKey = stripeSecretKey;
	}


    

    @PostMapping("/payment/charge")
    public String handlePayment(@RequestParam("stripeToken") String stripeToken,
                                @RequestParam("amount") int amount) {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount); // Amount in cents
        chargeParams.put("currency", "usd");
        chargeParams.put("source", stripeToken);

        try {
            Charge charge = Charge.create(chargeParams);
            return "Payment successful with charge ID: " + charge.getId();
        } catch (StripeException e) {
            e.printStackTrace();
            return "Payment failed: " + e.getMessage();
        }
    }
}
