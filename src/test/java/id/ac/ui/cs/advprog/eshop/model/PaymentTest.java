package model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Map<String, String> paymentData;
    private Order order;

    @BeforeEach
    void setup() {
        paymentData = new HashMap<>();
        order = new Order("order-1", new ArrayList<>(List.of(new Product())), 1L, "Sean");
    }

    @Test
    void testVoucherSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-1", order, "VOUCHER", paymentData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testBankTransferSuccess() {
        paymentData.put("bankName", "Bank UI");
        paymentData.put("referenceCode", "REF123");
        Payment payment = new Payment("pay-2", order, "BANK_TRANSFER", paymentData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testBankTransferRejected() {
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", null);
        Payment payment = new Payment("pay-3", order, "BANK_TRANSFER", paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }
}