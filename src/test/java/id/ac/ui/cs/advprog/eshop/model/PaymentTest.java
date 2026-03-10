package id.ac.ui.cs.advprog.eshop.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Payment payment = new VoucherPayment("pay-1", order, paymentData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testVoucherRejected_Not16Characters() {
        // Panjang kurang dari 16 karakter (contoh: 13 karakter)
        paymentData.put("voucherCode", "ESHOP12345678");
        Payment payment = new VoucherPayment("pay-2", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testVoucherRejected_NotStartsWithEshop() {
        // Panjang 16 dan ada 8 angka, tapi tidak diawali ESHOP
        paymentData.put("voucherCode", "XSHOP1234ABC5678");
        Payment payment = new VoucherPayment("pay-3", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testVoucherRejected_Not8Numerics() {
        // Panjang 16 dan diawali ESHOP, tapi jumlah angka bukan 8 (contoh: 3 angka)
        paymentData.put("voucherCode", "ESHOP123ABCDEFGH");
        Payment payment = new VoucherPayment("pay-4", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testBankTransferSuccess() {
        paymentData.put("bankName", "Bank UI");
        paymentData.put("referenceCode", "REF123");
        // UBAH BARIS INI: Gunakan BankTransferPayment
        Payment payment = new BankTransferPayment("pay-2", order, paymentData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testBankTransferRejected() {
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", null);
        // UBAH BARIS INI: Gunakan BankTransferPayment
        Payment payment = new BankTransferPayment("pay-3", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }
}