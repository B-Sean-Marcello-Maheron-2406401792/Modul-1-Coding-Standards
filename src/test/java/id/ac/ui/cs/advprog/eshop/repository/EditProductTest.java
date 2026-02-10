package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EditProductTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testEditProductPositive() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Sampo Cap Usep");
        updatedProduct.setProductQuantity(50);
        productRepository.update(updatedProduct);

        Product foundProduct = productRepository.findById(product.getProductId());
        assertEquals("Sampo Cap Usep", foundProduct.getProductName());
        assertEquals(50, foundProduct.getProductQuantity());
    }

    @Test
    void testEditProductNegative() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("invalid-id");
        nonExistentProduct.setProductName("Barang Palsu");

        productRepository.update(nonExistentProduct);

        Product originalProduct = productRepository.findById(product.getProductId());
        assertEquals("Sampo Cap Bambang", originalProduct.getProductName());
    }

    @Test
    void testEditProductNameOnly() {
        Product product = new Product();
        product.setProductId("id-awal");
        product.setProductName("Nama Lama");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("id-awal");
        updatedProduct.setProductName("Nama Baru");
        updatedProduct.setProductQuantity(10); // Kuantitas tetap sama
        productRepository.update(updatedProduct);

        Product foundProduct = productRepository.findById("id-awal");
        assertEquals("Nama Baru", foundProduct.getProductName());
        assertEquals(10, foundProduct.getProductQuantity());
    }

    @Test
    void testEditProductQuantityOnly() {
        Product product = new Product();
        product.setProductId("id-awal");
        product.setProductName("Barang Tetap");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("id-awal");
        updatedProduct.setProductName("Barang Tetap");
        updatedProduct.setProductQuantity(999); // Hanya kuantitas berubah
        productRepository.update(updatedProduct);

        Product foundProduct = productRepository.findById("id-awal");
        assertEquals(999, foundProduct.getProductQuantity());
        assertEquals("Barang Tetap", foundProduct.getProductName());
    }

    @Test
    void testEditOneOfMultipleProducts() {
        Product p1 = new Product();
        p1.setProductId("id-1");
        p1.setProductName("Produk 1");
        productRepository.create(p1);

        Product p2 = new Product();
        p2.setProductId("id-2");
        p2.setProductName("Produk 2");
        productRepository.create(p2);

        // Edit produk 2
        Product updatedP2 = new Product();
        updatedP2.setProductId("id-2");
        updatedP2.setProductName("Produk 2 Terupdate");
        productRepository.update(updatedP2);

        // Pastikan p1 tidak ikut berubah
        assertEquals("Produk 1", productRepository.findById("id-1").getProductName());
        // Pastikan p2 berubah
        assertEquals("Produk 2 Terupdate", productRepository.findById("id-2").getProductName());
    }

    @Test
    void testUpdateWithEmptyRepository() {
        Product product = new Product();
        product.setProductId("non-existent");
        product.setProductName("Ghost Product");

        // Memastikan tidak crash saat update repo yang kosong
        assertDoesNotThrow(() -> {
            productRepository.update(product);
        });
    }
}