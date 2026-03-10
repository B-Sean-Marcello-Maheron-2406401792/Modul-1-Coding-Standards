package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class,
        excludeAutoConfiguration = ThymeleafAutoConfiguration.class // Tambahkan baris ini
)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private List<Product> products;
    private Order order;

    @BeforeEach
    void setUp() {
        // Tambahkan minimal 1 produk agar lolos validasi list produk kosong (jika ada)
        products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("p1");
        products.add(product);

        // Gunakan 5 parameter (Id, Products, Time, Author, Status)
        // agar lolos validasi status di Order.java:26
        order = new Order("13652556-0128-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat", "WAITING_PAYMENT");
    }

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderCreate"));
    }

    @Test
    void testOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"));
    }

    @Test
    void testPostOrderHistory() throws Exception {
        when(orderService.findAllByAuthor(anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/order/history").param("author", "Sean"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"))
                .andExpect(model().attributeExists("orders"));

        verify(orderService, times(1)).findAllByAuthor("Sean");
    }

    @Test
    void testPayOrderPage() throws Exception {
        // Mock service menggunakan objek order yang sudah disiapkan di setUp
        when(orderService.findById(order.getId())).thenReturn(order);

        mockMvc.perform(get("/order/pay/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("payOrder"))
                .andExpect(model().attribute("order", order));

        verify(orderService, times(1)).findById(order.getId());
    }

    @Test
    void testPayOrderPost() throws Exception {
        String orderId = order.getId();
        String method = "BANK_TRANSFER";

        mockMvc.perform(post("/order/pay/" + orderId)
                        .param("method", method))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"))
                .andExpect(model().attribute("orderId", orderId))
                .andExpect(model().attribute("method", method));
    }
}