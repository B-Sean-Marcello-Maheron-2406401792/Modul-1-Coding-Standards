package id.ac.ui.cs.advprog.eshop.model;
import id.ac.ui.cs.advprog.eshop.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public abstract class Payment {
    private String id;
    private Order order;
    private String method;
    @Setter
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        this.id = id; this.order = order; this.method = method; this.paymentData = paymentData;
        this.status = determineStatus();
    }
    protected abstract String determineStatus();
}