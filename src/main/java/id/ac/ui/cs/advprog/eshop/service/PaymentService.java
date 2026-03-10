package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.List;
import java.util.Map;

public interface PaymentService {

    // Method untuk membuat objek payment baru dan menyimpannya ke repository
    Payment addPayment(Order order, String method, Map<String, String> paymentData);

    // Method untuk mengubah status payment (sekaligus akan mengubah status order terkait)
    Payment setStatus(Payment payment, String status);

    // Method untuk mengambil satu objek payment berdasarkan ID-nya
    Payment getPayment(String paymentId);

    // Method untuk mengambil seluruh objek payment yang ada
    List<Payment> getAllPayments();

}