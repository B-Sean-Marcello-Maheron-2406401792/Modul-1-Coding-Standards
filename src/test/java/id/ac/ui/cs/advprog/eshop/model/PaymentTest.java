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
        // Kasus: Semua kondisi terpenuhi (True && True && True && True)
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new VoucherPayment("pay-v1", order, paymentData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testVoucherRejected_Null() {
        // Kasus: v == null
        paymentData.put("voucherCode", null);
        Payment payment = new VoucherPayment("pay-v2", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testVoucherRejected_Not16Characters() {
        // Kasus: v.length() != 16 (Hanya 15 karakter)
        paymentData.put("voucherCode", "ESHOP12345678AB");
        Payment payment = new VoucherPayment("pay-v3", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testVoucherRejected_NotStartsWithEshop() {
        // Kasus: !v.startsWith("ESHOP")
        paymentData.put("voucherCode", "XSHOP1234ABC5678");
        Payment payment = new VoucherPayment("pay-v4", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testVoucherRejected_NotEnoughNumerics() {
        // Kasus: Jumlah angka != 8 (Hanya ada 7 angka)
        paymentData.put("voucherCode", "ESHOP1234ABC567X");
        Payment payment = new VoucherPayment("pay-v5", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testVoucherRejected_TooManyNumerics() {
        // Kasus: Jumlah angka != 8 (Ada 9 angka)
        paymentData.put("voucherCode", "ESHOP123456789AB");
        Payment payment = new VoucherPayment("pay-v6", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testBankTransferSuccess() {
        paymentData.put("bankName", "Bank Mandiri");
        paymentData.put("referenceCode", "REF123456");
        Payment payment = new BankTransferPayment("pay-1", order, paymentData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testBankTransferRejected_BankNameNull() {
        paymentData.put("bankName", null);
        paymentData.put("referenceCode", "REF123");
        Payment payment = new BankTransferPayment("pay-2", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testBankTransferRejected_BankNameEmpty() {
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "REF123");
        Payment payment = new BankTransferPayment("pay-3", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testBankTransferRejected_ReferenceCodeNull() {
        paymentData.put("bankName", "Bank Central Asia");
        paymentData.put("referenceCode", null);
        Payment payment = new BankTransferPayment("pay-4", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testBankTransferRejected_ReferenceCodeEmpty() {
        paymentData.put("bankName", "Bank Negara Indonesia");
        paymentData.put("referenceCode", "");
        Payment payment = new BankTransferPayment("pay-5", order, paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }
}