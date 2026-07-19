package ifpb.edu.br.consumer.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import ifpb.edu.br.consumer.entity.PaymentEntity;
import ifpb.edu.br.consumer.entity.PaymentMethod;
import ifpb.edu.br.consumer.repository.PaymentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentGatewayService {

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    private final PaymentRepository paymentRepository;

    public PaymentIntent charge(PaymentEntity payment) throws StripeException {
        PaymentMethod paymentMethod = payment.getUser().getPaymentMethod();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(payment.getValue().longValue())
                .setCurrency("brl")
                .setDescription(payment.getName())
                .setPaymentMethod(paymentMethod.getToken())
                .setConfirm(true)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build()
                )
                .build();

        return PaymentIntent.create(params);
    }
}