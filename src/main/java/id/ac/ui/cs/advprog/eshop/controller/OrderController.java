package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import id.ac.ui.cs.advprog.eshop.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "orderCreate";
    }

    @GetMapping("/history")
    public String historyOrderPage() {
        return "orderHistory";
    }

    @PostMapping("/history")
    public String showOrderHistory(@RequestParam String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "orderHistory";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "payOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable String orderId, @RequestParam String method, Model model) {
        // Logika untuk redirect ke halaman detail pembayaran dengan ID
        // Secara sederhana kita teruskan metode ke halaman selanjutnya
        model.addAttribute("orderId", orderId);
        model.addAttribute("method", method);
        return "paymentDetail";
    }
}