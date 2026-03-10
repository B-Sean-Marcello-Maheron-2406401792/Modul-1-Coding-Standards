package id.ac.ui.cs.advprog.eshop.service;
import id.ac.ui.cs.advprog.eshop.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @InjectMocks PaymentServiceImpl paymentService;
    @Mock PaymentRepository paymentRepository;
    private Order order;

    @BeforeEach
    void setup() {
        order = new Order("order-1", new ArrayList<>(List.of(new Product())), 1L, "Sean");
    }

    @Test
    void testAddPaymentBankTransfer() {
        Payment p = new BankTransferPayment("1", order, new HashMap<>());
        doReturn(p).when(paymentRepository).save(any(Payment.class));
        Payment res = paymentService.addPayment(order, "BANK_TRANSFER", new HashMap<>());
        assertNotNull(res);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucher() {
        // Mengetes branch createPaymentObject untuk VOUCHER
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        Payment p = new VoucherPayment("pay-voucher", order, data);

        doReturn(p).when(paymentRepository).save(any(Payment.class));

        Payment res = paymentService.addPayment(order, "VOUCHER", data);

        assertNotNull(res);
        assertTrue(res instanceof VoucherPayment);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentUnsupportedMethod() {
        // Mengetes branch throw IllegalArgumentException
        Map<String, String> data = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.addPayment(order, "GOPAY", data);
        });
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        // Mengetes branch if ("SUCCESS".equals(status))
        Payment payment = new BankTransferPayment("pay-1", order, new HashMap<>());
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", result.getStatus());
        assertEquals("SUCCESS", order.getStatus()); // Status order harus ikut berubah jadi SUCCESS
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejected() {
        // Mengetes branch else if ("REJECTED".equals(status))
        Payment payment = new BankTransferPayment("pay-1", order, new HashMap<>());
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", result.getStatus());
        assertEquals("FAILED", order.getStatus()); // Status order harus berubah jadi FAILED
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusOther() {
        // Mengetes branch else (status selain SUCCESS/REJECTED)
        Payment payment = new BankTransferPayment("pay-1", order, new HashMap<>());
        order.setStatus("WAITING_PAYMENT");
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "PENDING");

        assertEquals("PENDING", result.getStatus());
        assertEquals("WAITING_PAYMENT", order.getStatus()); // Status order tidak boleh berubah
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testGetPayment() {
        // Mengetes delegasi ke repository.findById
        Payment payment = new BankTransferPayment("pay-1", order, new HashMap<>());
        doReturn(payment).when(paymentRepository).findById("pay-1");

        Payment result = paymentService.getPayment("pay-1");

        assertNotNull(result);
        assertEquals("pay-1", result.getId());
        verify(paymentRepository, times(1)).findById("pay-1");
    }

    @Test
    void testGetAllPayments() {
        // Mengetes delegasi ke repository.getAllPayments
        List<Payment> payments = new ArrayList<>();
        payments.add(new BankTransferPayment("pay-1", order, new HashMap<>()));
        doReturn(payments).when(paymentRepository).getAllPayments();

        List<Payment> result = paymentService.getAllPayments();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(paymentRepository, times(1)).getAllPayments();
    }
}