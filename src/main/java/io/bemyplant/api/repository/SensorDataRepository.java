package io.bemyplant.api.repository;

import io.bemyplant.api.entity.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SensorDataRepository extends MongoRepository<SensorData, String> {
}
