package edu.springboot.completablefuture.reposit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.springboot.completablefuture.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

}