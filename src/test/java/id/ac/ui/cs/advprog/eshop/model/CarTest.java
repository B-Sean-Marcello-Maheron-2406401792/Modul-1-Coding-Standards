package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarTest {
    @Test
    void testUpdateCar() {
        Car car = new Car();
        car.setCarId("1");
        car.setCarName("Toyota");

        Car updatedCar = new Car();
        updatedCar.setCarName("Honda");
        updatedCar.setCarColor("Red");
        updatedCar.setCarQuantity(10);

        car.update(updatedCar);

        assertEquals("Honda", car.getCarName());
        assertEquals("Red", car.getCarColor());
        assertEquals(10, car.getCarQuantity());
    }
}
