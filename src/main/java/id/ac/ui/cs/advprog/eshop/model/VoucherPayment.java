package id.ac.ui.cs.advprog.eshop.model;
import java.util.Map;

public class VoucherPayment extends Payment {
    public VoucherPayment(String id, Order order, Map<String, String> paymentData) {
        super(id, order, "VOUCHER", paymentData);
    }
    @Override
    protected String determineStatus() {
        String v = getPaymentData().get("voucherCode");
        return (v != null && v.length() == 16 && v.startsWith("ESHOP") && v.replaceAll("[^0-9]", "").length() == 8)
                ? "SUCCESS" : "REJECTED";
    }
}