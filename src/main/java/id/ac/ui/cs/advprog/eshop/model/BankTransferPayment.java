package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

public class BankTransferPayment extends Payment {
    private static final String BANK_NAME_KEY = "bankName";
    private static final String REFERENCE_CODE_KEY = "referenceCode";

    public BankTransferPayment(String id, Order order, Map<String, String> paymentData) {
        super(id, order, "BANK_TRANSFER", paymentData);
    }

    @Override
    protected String determineStatus() {
        String bankName = getPaymentData().get(BANK_NAME_KEY);
        String referenceCode = getPaymentData().get(REFERENCE_CODE_KEY);
        return (hasText(bankName) && hasText(referenceCode)) ? "SUCCESS" : "REJECTED";
    }
}
