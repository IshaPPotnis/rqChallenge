package com.example.rqchallenge.service;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeDetailsWriteServiceTest {

    private EmployeeDetailsWriteService employeeDetailsWriteService;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        employeeDetailsWriteService = new EmployeeDetailsWriteService();
        ReflectionTestUtils.setField(employeeDetailsWriteService, "dummyRestApi", "dummyApi");
        ReflectionTestUtils.setField(employeeDetailsWriteService, "restTemplate", restTemplate);
    }

    @Test
    void insertEmployeeData() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "abcd");
        data.put("age", 43);
        data.put("salary", 12000);

        final Map<String, Object> body = new LinkedHashMap();
        body.put("id", 1);
        body.put("name", data.get("name"));
        body.put("salary", data.get("salary"));
        body.put("age", data.get("age"));

        final JSONObject build = new JSONObject();
        build.put("data", body);

        ResponseEntity<JSONObject> mockResponse = new ResponseEntity<>(build, HttpStatus.OK);

        when(restTemplate.postForEntity("dummyApi/create", data, JSONObject.class)).thenReturn(mockResponse);

        employeeDetailsWriteService.insertEmployeeData(data);
    }

    @Test
    void deleteEmployeeDataById() {
        try {
            employeeDetailsWriteService.deleteEmployeeDataById("1");
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}