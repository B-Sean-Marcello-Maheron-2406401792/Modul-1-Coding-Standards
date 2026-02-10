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
    void testDeleteProductPositive() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        productRepository.delete(product.getProductId());

        Product foundProduct = productRepository.findById(product.getProductId());
        assertNull(foundProduct);
    }

    @Test
    void testDeleteProductNegative() {
        assertDoesNotThrow(() -> {
            productRepository.delete("non-existent-id");
        });
    }
}