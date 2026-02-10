package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DeleteProductTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testDeleteProductPositive() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        productRepository.delete(product.getProductId());

        Product foundProduct = productRepository.findById(product.getProductId());
        assertNull(foundProduct);

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProductNegative() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        // Mencoba menghapus dengan ID yang salah/tidak ada
        productRepository.delete("id-salah");

        // Produk asli seharusnya tetap ada
        Product foundProduct = productRepository.findById(product.getProductId());
        assertNotNull(foundProduct);
        assertEquals("Sampo Cap Bambang", foundProduct.getProductName());
    }

    @Test
    void testDeleteOnlyOneOfMultipleProducts() {
        Product product1 = new Product();
        product1.setProductId("id-1");
        product1.setProductName("Product 1");
        product1.setProductQuantity(10);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("id-2");
        product2.setProductName("Product 2");
        product2.setProductQuantity(20);
        productRepository.create(product2);

        // Hapus produk pertama
        productRepository.delete("id-1");

        // Pastikan produk 1 hilang, tapi produk 2 tetap ada
        assertNull(productRepository.findById("id-1"));
        assertNotNull(productRepository.findById("id-2"));
        assertEquals("Product 2", productRepository.findById("id-2").getProductName());
    }

    @Test
    void testDeleteProductWithEmptyRepository() {
        // Mencoba menghapus di repository yang kosong
        assertDoesNotThrow(() -> {
            productRepository.delete("any-id");
        });

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProductTwice() {
        Product product = new Product();
        product.setProductId("id-tetap");
        product.setProductName("Barang");
        product.setProductQuantity(10);
        productRepository.create(product);

        // Hapus pertama kali
        productRepository.delete("id-tetap");
        assertNull(productRepository.findById("id-tetap"));

        // Hapus kedua kali untuk ID yang sama (idempotent test)
        assertDoesNotThrow(() -> {
            productRepository.delete("id-tetap");
        });
        assertNull(productRepository.findById("id-tetap"));
    }
}