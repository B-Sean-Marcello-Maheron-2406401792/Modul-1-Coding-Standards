package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() throws Exception {
        ProductRepository productRepository = new ProductRepository();
        productService = new ProductServiceImpl();

        // Inject repository manual karena tidak pakai Spring / Mockito
        Field field = ProductServiceImpl.class.getDeclaredField("productRepository");
        field.setAccessible(true);
        field.set(productService, productRepository);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setProductName("Laptop");
        product.setProductQuantity(5);

        Product result = productService.create(product);

        assertNotNull(result.getProductId());
        assertEquals("Laptop", result.getProductName());
    }

    @Test
    void testFindAllProducts() {
        Product product = new Product();
        product.setProductName("Mouse");
        product.setProductQuantity(10);

        productService.create(product);

        List<Product> products = productService.findAll();

        assertEquals(1, products.size());
        assertEquals("Mouse", products.get(0).getProductName());
    }

    @Test
    void testFindById() {
        Product product = new Product();
        product.setProductName("Keyboard");
        product.setProductQuantity(3);

        Product saved = productService.create(product);

        Product found = productService.findById(saved.getProductId());

        assertNotNull(found);
        assertEquals("Keyboard", found.getProductName());
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setProductName("Monitor");
        product.setProductQuantity(2);

        Product saved = productService.create(product);

        saved.setProductName("Updated Monitor");
        productService.update(saved);

        Product updated = productService.findById(saved.getProductId());

        assertEquals("Updated Monitor", updated.getProductName());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setProductName("Printer");
        product.setProductQuantity(1);

        Product saved = productService.create(product);

        productService.delete(saved.getProductId());

        Product deleted = productService.findById(saved.getProductId());

        assertNull(deleted);
    }
}