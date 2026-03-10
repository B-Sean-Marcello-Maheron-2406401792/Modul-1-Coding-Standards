package id.ac.ui.cs.advprog.eshop.service;

import enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.BankTransferPayment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.VoucherPayment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String METHOD_VOUCHER = "VOUCHER";
    private static final String METHOD_BANK_TRANSFER = "BANK_TRANSFER";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_REJECTED = "REJECTED";

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String paymentId = UUID.randomUUID().toString();
        Payment payment = createPaymentObject(paymentId, order, method, paymentData);
        return paymentRepository.save(payment);
    }

    private Payment createPaymentObject(String id, Order order, String method, Map<String, String> data) {
        return switch (method) {
            case METHOD_VOUCHER -> new VoucherPayment(id, order, data);
            case METHOD_BANK_TRANSFER -> new BankTransferPayment(id, order, data);
            default -> throw new IllegalArgumentException("Unsupported payment method");
        };
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        synchronizeOrderStatus(payment.getOrder(), status);
        return paymentRepository.save(payment);
    }

    private void synchronizeOrderStatus(Order order, String paymentStatus) {
        if (STATUS_SUCCESS.equals(paymentStatus)) {
            order.setStatus(OrderStatus.SUCCESS.getValue());
        } else if (STATUS_REJECTED.equals(paymentStatus)) {
            order.setStatus(OrderStatus.FAILED.getValue());
        }
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }
}
