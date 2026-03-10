package id.ac.ui.cs.advprog.eshop.model;
import java.util.Map;

public class BankTransferPayment extends Payment {
    public BankTransferPayment(String id, Order order, Map<String, String> paymentData) {
        super(id, order, "BANK_TRANSFER", paymentData);
    }
    @Override
    protected String determineStatus() {
        String b = getPaymentData().get("bankName"); String r = getPaymentData().get("referenceCode");
        return (b != null && !b.isEmpty() && r != null && !r.isEmpty()) ? "SUCCESS" : "REJECTED";
    }
}