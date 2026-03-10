package id.ac.ui.cs.advprog.eshop.repository;
import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    CarRepositoryImpl carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepositoryImpl();
    }

    @Test
    void testCreateAndFindAll() {
        Car car = new Car();
        car.setCarId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        car.setCarName("Toyota Supra");
        car.setCarQuantity(1);

        carRepository.create(car);
        Iterator<Car> carIterator = carRepository.findAll();

        assertTrue(carIterator.hasNext());
        Car savedCar = carIterator.next();
        assertEquals(car.getCarId(), savedCar.getCarId());
        assertEquals(car.getCarName(), savedCar.getCarName());
    }

    @Test
    void testFindByIdSuccess() {
        Car car = new Car();
        car.setCarId("1");
        carRepository.create(car);

        Car foundCar = carRepository.findById("1");
        assertNotNull(foundCar);
        assertEquals("1", foundCar.getCarId());
    }

    @Test
    void testFindByIdNotFound() {
        Car car = new Car();
        car.setCarId("1");
        carRepository.create(car);

        Car foundCar = carRepository.findById("2"); // Branch: ID tidak ditemukan
        assertNull(foundCar);
    }

    @Test
    void testUpdateSuccess() {
        // Persiapan data awal
        Car car = new Car();
        car.setCarId("1");
        car.setCarName("Toyota Supra");
        car.setCarColor("White");
        car.setCarQuantity(1);
        carRepository.create(car);

        // Data baru untuk update
        Car updatedCar = new Car();
        updatedCar.setCarName("Honda Civic");
        updatedCar.setCarColor("Black");
        updatedCar.setCarQuantity(5);

        // Eksekusi update
        Car result = carRepository.update("1", updatedCar);

        // Verifikasi (Branch: ID ditemukan)
        assertNotNull(result);
        assertEquals("Honda Civic", result.getCarName());
        assertEquals("Black", result.getCarColor());
        assertEquals(5, result.getCarQuantity());
        assertEquals("1", result.getCarId()); // ID tidak boleh berubah
    }

    @Test
    void testUpdateNotFound() {
        // Persiapan data
        Car car = new Car();
        car.setCarId("1");
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("New Car");

        // Eksekusi update dengan ID salah
        Car result = carRepository.update("non-existent-id", updatedCar);

        // Verifikasi (Branch: Loop selesai tanpa memenuhi kondisi IF)
        assertNull(result);
    }

    @Test
    void testUpdateCarNotFound() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Tesla");
        Car result = carRepository.update("non-existent-id", updatedCar);
        assertNull(result);
    }

    @Test
    void testDelete() {
        Car car = new Car();
        car.setCarId("1");
        carRepository.create(car);

        carRepository.delete("1");
        Car foundCar = carRepository.findById("1");
        assertNull(foundCar);
    }
}