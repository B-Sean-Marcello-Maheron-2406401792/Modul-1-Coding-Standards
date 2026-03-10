package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @InjectMocks
    CarServiceImpl carService;

    @Mock
    CarRepositoryImpl carRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateCarWithId() {
        Car car = new Car();
        car.setCarId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        when(carRepository.create(car)).thenReturn(car);

        Car result = carService.create(car);

        // Branch: carId != null, UUID.randomUUID() tidak dipanggil
        assertEquals(car.getCarId(), result.getCarId());
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void testCreateCarWithoutId() {
        Car car = new Car();
        car.setCarId(null);
        when(carRepository.create(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Car result = carService.create(car);

        // Branch: carId == null, UUID.randomUUID() dipanggil
        assertNotNull(result.getCarId());
        verify(carRepository, times(1)).create(any(Car.class));
    }

    @Test
    void testFindAll() {
        List<Car> carList = new ArrayList<>();
        Car car = new Car();
        carList.add(car);

        Iterator<Car> iterator = carList.iterator();
        when(carRepository.findAll()).thenReturn(iterator);

        List<Car> result = carService.findAll();

        assertEquals(1, result.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Car car = new Car();
        car.setCarId("1");
        when(carRepository.findById("1")).thenReturn(car);

        Car result = carService.findById("1");

        assertEquals("1", result.getCarId());
        verify(carRepository, times(1)).findById("1");
    }

    @Test
    void testUpdate() {
        Car car = new Car();
        carService.update("1", car);

        // Verifikasi delegasi ke repository
        verify(carRepository, times(1)).update("1", car);
    }

    @Test
    void testDeleteCarById() {
        carService.deleteCarById("1");

        // Verifikasi delegasi ke repository
        verify(carRepository, times(1)).delete("1");
    }
}