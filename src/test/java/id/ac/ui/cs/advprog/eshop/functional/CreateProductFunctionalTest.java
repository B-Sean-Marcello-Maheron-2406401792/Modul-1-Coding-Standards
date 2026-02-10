package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("http://localhost:%d", serverPort);
    }

    @Test
    void createProduct_isCorrect(ChromeDriver driver) {
        driver.get(baseUrl + "/product/create");

        String inputName = "Sampo Cap Bambang";
        String inputQuantity = "100";

        driver.findElement(By.id("nameInput")).sendKeys(inputName);
        driver.findElement(By.id("quantityInput")).sendKeys(inputQuantity);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Gunakan Wait agar tidak failed karena race condition
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/product/list"));

        WebElement table = driver.findElement(By.tagName("table"));
        assertTrue(table.getText().contains(inputName), "Product name should be visible in the table");
    }

    @Test
    void createProduct_withZeroQuantity_isCorrect(ChromeDriver driver) {
        driver.get(baseUrl + "/product/create");

        String inputName = "Produk Gratis";
        String inputQuantity = "0";

        driver.findElement(By.id("nameInput")).sendKeys(inputName);
        driver.findElement(By.id("quantityInput")).sendKeys(inputQuantity);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/product/list"));

        WebElement table = driver.findElement(By.tagName("table"));
        assertTrue(table.getText().contains(inputName));
        assertTrue(table.getText().contains("0"));
    }

    @Test
    void createProduct_withEmptyName_shouldStayOnCreatePage(ChromeDriver driver) {
        driver.get(baseUrl + "/product/create");

        driver.findElement(By.id("nameInput")).clear();
        driver.findElement(By.id("quantityInput")).sendKeys("10");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verifikasi URL tidak berubah ke /list (karena validasi menahan submit)
        String currentUrl = driver.getCurrentUrl();
        assertFalse(currentUrl.contains("/product/list"), "Should not redirect if name is empty");
    }

    @Test
    void createMultipleProducts_allShouldBeVisibleInList(ChromeDriver driver) {
        String[][] products = {
                {"Product Alpha", "10"},
                {"Product Beta", "20"},
                {"Product Gamma", "30"}
        };

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        for (String[] product : products) {
            driver.get(baseUrl + "/product/create");

            // Tunggu input muncul sebelum mengetik
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameInput"))).sendKeys(product[0]);
            driver.findElement(By.id("quantityInput")).sendKeys(product[1]);
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // Tunggu sampai balik ke list
            wait.until(ExpectedConditions.urlContains("/product/list"));
        }

        WebElement table = driver.findElement(By.tagName("table"));
        String tableContent = table.getText();
        for (String[] product : products) {
            assertTrue(tableContent.contains(product[0]), "Could not find product: " + product[0]);
        }
    }
}