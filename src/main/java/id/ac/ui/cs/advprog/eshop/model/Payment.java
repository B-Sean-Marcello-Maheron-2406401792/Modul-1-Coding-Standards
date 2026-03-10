package model;
import id.ac.ui.cs.advprog.eshop.model.Order;
import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    private String id;
    private Order order;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        this.id = id; this.order = order; this.method = method; this.paymentData = paymentData;

        if ("VOUCHER".equals(method)) {
            String v = paymentData.get("voucherCode");
            if (v != null && v.length() == 16 && v.startsWith("ESHOP") && v.replaceAll("[^0-9]", "").length() == 8) {
                this.status = "SUCCESS";
            } else this.status = "REJECTED";
        } else if ("BANK_TRANSFER".equals(method)) {
            String b = paymentData.get("bankName"); String r = paymentData.get("referenceCode");
            if (b == null || b.isEmpty() || r == null || r.isEmpty()) this.status = "REJECTED";
            else this.status = "SUCCESS";
        } else {
            this.status = "REJECTED";
        }
    }
    public void setStatus(String status) { this.status = status; }
}