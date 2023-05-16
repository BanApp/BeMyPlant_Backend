package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.SensorData;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.service.UserService;
import me.silvernine.tutorial.service.SensorDataService;
import me.silvernine.tutorial.util.SecurityUtil;
import org.bson.BsonDocument;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final SensorDataService sensorDataService;

    public UserController(UserService userService, SensorDataService sensorDataService) {
        this.userService = userService;
        this.sensorDataService = sensorDataService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }

    //회원 가입
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    //센서 데이터 넣기
    @PostMapping("/post-sensor-data")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createSensorData(@RequestBody SensorData sensorData) {
        SensorData createdSensorData = sensorDataService.createSensorData(sensorData);
        return ResponseEntity.ok("Successfully created sensor data with id: " + createdSensorData.getId());
    }

    //사용자 데이터 얻기
    /*
    @GetMapping("/get-sensor-data")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SensorData> getRecentSensorData() {
        String username = SecurityUtil.getCurrentUsername().orElse("unknown");
        SensorData sensorData = sensorDataService.getRecentSensorData(username);
        if (sensorData != null) {
            return ResponseEntity.ok(sensorData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    //사용자 데이터 얻기
    @GetMapping("/get-sensor-data")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SensorData>> getRecentSensorData(@RequestParam("num") int num) {
        String username = SecurityUtil.getCurrentUsername().orElse("unknown");
        List<SensorData> sensorDataList = sensorDataService.getRecentSensorData(username, num);
        if (!sensorDataList.isEmpty()) {
            return ResponseEntity.ok(sensorDataList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //사용자 정보 조회
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DeleteMapping("/withdrawal")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> withdrawal(HttpServletRequest request) {
        String user = SecurityUtil.getCurrentUsername().orElse("unknown");

        // H2 데이터베이스에서 해당 사용자 컬럼 삭제

        // "user" 테이블에서 "username"과 일치하는 사용자의 "user_id" 조회
        String getUserIdQuery = "SELECT user_id FROM \"user\" WHERE username = ?";
        Long user_id_cl = jdbcTemplate.queryForObject(getUserIdQuery, Long.class, user);

        // user_authority 테이블에서 해당 사용자의 권한 정보 삭제
        String deleteAuthorityQuery = "DELETE FROM user_authority WHERE user_id = ?";
        jdbcTemplate.update(deleteAuthorityQuery, user_id_cl);

        // user 테이블에서 해당 사용자 삭제
        String deleteUserQuery = "DELETE FROM \"user\" WHERE username = ?";
        jdbcTemplate.update(deleteUserQuery, user);

        // MongoDB에서 해당 사용자 컬렉션 삭제
        mongoTemplate.dropCollection(user);

        return ResponseEntity.ok("Withdrawal successful");
    }




}