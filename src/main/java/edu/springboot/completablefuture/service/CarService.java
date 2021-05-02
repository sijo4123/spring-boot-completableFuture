package edu.springboot.completablefuture.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.springboot.completablefuture.entity.Car;
import edu.springboot.completablefuture.reposit.CarRepository;

@Service
public class CarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarService.class);

    @Autowired
    private CarRepository carRepository;

    @Async
    public CompletableFuture<List<Car>> saveCars(final MultipartFile file) throws Exception {
        final long start = System.currentTimeMillis();

        List<Car> cars = parseCSVFile(file);

        LOGGER.info("Saving a list of cars of size {} records", cars.size());

        cars = carRepository.saveAll(cars);

        LOGGER.info("Elapsed time: {}", (System.currentTimeMillis() - start));
        return CompletableFuture.completedFuture(cars);
    }

    private List<Car> parseCSVFile(final MultipartFile file) throws Exception {
        final List<Car> cars=new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line=br.readLine()) != null) {
                    final String[] data=line.split(";");
                    final Car car=new Car();
                    car.setManufacturer(data[0]);
                    car.setModel(data[1]);
                    car.setType(data[2]);
                    cars.add(car);
                }
                return cars;
            }
        } catch(final IOException e) {
            LOGGER.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }

    @Async
    public CompletableFuture<List<Car>> getAllCars() {

        LOGGER.info("Request to get a list of cars");
        final List<Car> cars = carRepository.findAll();
        try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return CompletableFuture.completedFuture(cars);
    }
}