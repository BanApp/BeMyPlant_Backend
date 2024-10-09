package io.bemyplant.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.bemyplant.api.entity.SensorData;
import io.bemyplant.api.repository.SensorDataRepository;
import io.bemyplant.api.util.SecurityUtil;

import java.util.List;


@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    //데이터 삽입
    public SensorData createSensorData(SensorData sensorData) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        mongoTemplate.save(sensorData, username);
        return sensorData;
    }


    //최신 데이터 n개 조회
    public List<SensorData> getRecentSensorData(String username, int num) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "id")).limit(num);
        return mongoTemplate.find(query, SensorData.class, username);
    }

}

