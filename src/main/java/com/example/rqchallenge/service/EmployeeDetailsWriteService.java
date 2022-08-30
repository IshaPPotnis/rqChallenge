package com.example.rqchallenge.service;

import com.example.rqchallenge.exception.DummyAPIException;
import com.example.rqchallenge.model.Employee;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Log4j2
public class EmployeeDetailsWriteService {

    @Value("${dummy.rest.api}")
    String dummyRestApi;
    @Autowired
    RestTemplate restTemplate;

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5000))
    public Employee insertEmployeeData(Map<String, Object> employeeInput) {
        try {
            final String uri = dummyRestApi + "/create";
            final ResponseEntity<JSONObject> employeeResponseEntity = restTemplate.postForEntity(uri, employeeInput, JSONObject.class);
            LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) Objects.requireNonNull(employeeResponseEntity.getBody()).get("data");

            return Employee.builder()
                    .id(Long.valueOf((Integer) data.get("id")))
                    .employee_name((String) data.get("name"))
                    .employee_salary(Long.valueOf((Integer) data.get("salary")))
                    .employee_age(Long.valueOf((Integer) data.get("age")))
                    .build();
        } catch (HttpServerErrorException.InternalServerError exception) {
            throw new DummyAPIException("Message : " + exception.getMessage());
        }
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 3000))
    public void deleteEmployeeDataById(String empId) {
        try {
            final String uri = dummyRestApi + "/delete/" + empId;
            restTemplate.delete(uri);
        } catch (HttpServerErrorException.InternalServerError exception) {
            throw new DummyAPIException("Message : " + exception.getMessage());
        }
    }
}
