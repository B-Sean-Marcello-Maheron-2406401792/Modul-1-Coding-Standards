package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CarController.class,
excludeAutoConfiguration = org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.class
)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("123-abc");
        car.setCarName("Tesla Model S");
        car.setCarColor("Midnight Silver");
        car.setCarQuantity(5);
    }

    @Test
    void testCreateCarPage() throws Exception {
        mockMvc.perform(get("/car/createCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("createCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void testCreateCarPost() throws Exception {
        mockMvc.perform(post("/car/createCar")
                        .flashAttr("car", car))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).create(any(Car.class));
    }

    @Test
    void testCarListPage() throws Exception {
        List<Car> allCars = new ArrayList<>();
        allCars.add(car);
        when(carService.findAll()).thenReturn(allCars);

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"))
                .andExpect(model().attribute("cars", allCars));
    }

    @Test
    void testEditCarPage() throws Exception {
        when(carService.findById("123-abc")).thenReturn(car);

        mockMvc.perform(get("/car/editCar/123-abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("editCar"))
                .andExpect(model().attribute("car", car));
    }

    @Test
    void testEditCarPost() throws Exception {
        mockMvc.perform(post("/car/editCar")
                        .flashAttr("car", car))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).update(eq("123-abc"), any(Car.class));
    }

    @Test
    void testDeleteCarPost() throws Exception {
        mockMvc.perform(post("/car/deleteCar")
                        .param("carId", "123-abc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).deleteCarById("123-abc");
    }
}