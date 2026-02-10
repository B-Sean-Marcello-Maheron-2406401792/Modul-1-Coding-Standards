package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class HomePageFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void pageTitle_isCorrect(ChromeDriver driver) {
        driver.get(baseUrl);
        String pageTitle = driver.getTitle();

        // Di desain baru, title di <head> adalah "ADV Shop - Home"
        assertEquals("ADV Shop - Home", pageTitle);
    }

    @Test
    void welcomeMessage_homePage_isCorrect(ChromeDriver driver) {
        driver.get(baseUrl);

        // Di desain baru, judul utama menggunakan tag <h1> dengan teks "ADV Shop"
        // Jika Anda ingin tetap mengecek kata "Welcome", kita bisa cek di tag <p>
        String welcomeMessage = driver.findElement(By.tagName("h1")).getText();
        assertEquals("ADV Shop", welcomeMessage);
    }

    @Test
    void checkLeadParagraph_isCorrect(ChromeDriver driver) {
        driver.get(baseUrl);
        // Mengecek apakah teks selamat datang muncul di paragraf lead
        String leadText = driver.findElement(By.className("lead")).getText();
        assertTrue(leadText.contains("Welcome to ADV Shop!"));
    }
}