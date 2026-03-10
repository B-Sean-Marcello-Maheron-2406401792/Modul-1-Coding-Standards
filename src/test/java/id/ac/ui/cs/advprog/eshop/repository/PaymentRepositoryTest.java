package id.ac.ui.cs.advprog.eshop.repository;
import id.ac.ui.cs.advprog.eshop.model.BankTransferPayment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Payment payment;

    @BeforeEach
    void setup() {
        paymentRepository = new PaymentRepository();

        // Setup data dummy
        List<Product> products = new ArrayList<>();
        Product p = new Product();
        p.setProductId("1");
        p.setProductName("Sabun");
        p.setProductQuantity(1);
        products.add(p);

        Order order = new Order("order-1", products, 1708560000L, "Sean");

        // Menggunakan BankTransferPayment karena Payment adalah abstract class
        payment = new BankTransferPayment("pay-1", order, new HashMap<>());
    }

    @Test
    void testSaveAndFindById() {
        paymentRepository.save(payment);
        Payment findResult = paymentRepository.findById("pay-1");
        assertNotNull(findResult);
        assertEquals(payment.getId(), findResult.getId());
    }

    @Test
    void testGetAllPayments() {
        paymentRepository.save(payment);
        List<Payment> allPayments = paymentRepository.getAllPayments();
        assertEquals(1, allPayments.size());
    }
}