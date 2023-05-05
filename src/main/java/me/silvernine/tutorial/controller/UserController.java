package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.entity.SensorData;
import me.silvernine.tutorial.service.UserService;
import me.silvernine.tutorial.service.SensorDataService;
import me.silvernine.tutorial.util.SecurityUtil;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;

import org.springframework.data.mongodb.core.MongoTemplate;

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
    @PostMapping("/sensor-data")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createSensorData(@RequestBody SensorData sensorData) {
        SensorData createdSensorData = sensorDataService.createSensorData(sensorData);
        return ResponseEntity.ok("Successfully created sensor data with id: " + createdSensorData.getId());
    }

    //사용자 데이터 얻기
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
}