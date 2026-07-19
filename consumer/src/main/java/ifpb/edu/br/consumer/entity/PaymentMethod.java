package ifpb.edu.br.consumer.entity;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    VISA("pm_card_visa"),
    MASTERCARD("pm_card_mastercard"),
    DECLINED_GENERIC("pm_card_chargeDeclined"),
    DECLINED_INSUFFICIENT_FUNDS("pm_card_chargeDeclinedInsufficientFunds"),
    DECLINED_FRAUDULENT("pm_card_chargeDeclinedFraudulent");

    private final String token;

    PaymentMethod(String token) {
        this.token = token;
    }
}