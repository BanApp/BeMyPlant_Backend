package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SensorDataRepository extends MongoRepository<SensorData, String> {
}
