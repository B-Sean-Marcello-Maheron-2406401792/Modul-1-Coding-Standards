package id.ac.ui.cs.advprog.eshop.service;

import enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = createPaymentObject(UUID.randomUUID().toString(), order, method, paymentData);
        return paymentRepository.save(payment);
    }

    private Payment createPaymentObject(String id, Order order, String method, Map<String, String> data) {
        if ("VOUCHER".equals(method)) return new VoucherPayment(id, order, data);
        if ("BANK_TRANSFER".equals(method)) return new BankTransferPayment(id, order, data);
        throw new IllegalArgumentException("Unsupported payment method");
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        if ("SUCCESS".equals(status)) payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        else if ("REJECTED".equals(status)) payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) { return paymentRepository.findById(paymentId); }

    @Override
    public List<Payment> getAllPayments() { return paymentRepository.getAllPayments(); }
}