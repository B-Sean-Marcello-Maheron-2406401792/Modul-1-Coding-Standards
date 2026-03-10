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
        String id = UUID.randomUUID().toString();
        Payment payment;
        if ("VOUCHER".equals(method)) payment = new VoucherPayment(id, order, paymentData);
        else if ("BANK_TRANSFER".equals(method)) payment = new BankTransferPayment(id, order, paymentData);
        else throw new IllegalArgumentException("Method not supported");

        return paymentRepository.save(payment);
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