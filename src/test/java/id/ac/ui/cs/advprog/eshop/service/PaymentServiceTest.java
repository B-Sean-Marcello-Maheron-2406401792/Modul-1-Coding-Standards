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
}