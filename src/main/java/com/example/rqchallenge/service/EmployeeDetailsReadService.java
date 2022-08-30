package com.example.rqchallenge.service;

import com.example.rqchallenge.exception.DummyAPIException;
import com.example.rqchallenge.exception.InvalidDataException;
import com.example.rqchallenge.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
public class EmployeeDetailsReadService {

    @Value("${dummy.rest.api}")
    String dummyRestApi;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    JSONParser jsonParser;

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5000))
    public List<Employee> getEmployeeDetails() {
        try {
            final String uri = dummyRestApi + "/employees";
            final String result = restTemplate.getForObject(uri, String.class);
            List<Employee> emps = new ArrayList<>();
            JSONObject jsonResponse = (JSONObject) jsonParser.parse(result);
            JSONArray data = (JSONArray) Objects.requireNonNull(jsonResponse).get("data");
            data.forEach(e ->
                    emps.add(objectMapper.convertValue(e, Employee.class))
            );
            return emps;
        } catch (ParseException e) {
            throw new DummyAPIException("Parse exception : " + e.getMessage());
        } catch (HttpServerErrorException.InternalServerError exception) {
            throw new DummyAPIException("Message : " + exception.getMessage());
        }
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5000))
    public Employee getEmployeeByEmployeeId(String id) {
        if (id == null) throw new InvalidDataException("Id cannot be null");
        try {
            Long.parseLong(id);
        } catch (NumberFormatException numberFormatException) {
            throw new InvalidDataException("Id is not Valid");
        }
        try {
            final String uri = dummyRestApi + "/employee/" + id;

            JSONObject jsonResponse = (JSONObject) jsonParser.parse(restTemplate.getForObject(uri, String.class));
            JSONObject data = (JSONObject) Objects.requireNonNull(jsonResponse).get("data");

            return objectMapper.convertValue(data, Employee.class);
        } catch (ParseException e) {
            log.error("Parse exception : " + e.getMessage());
            throw new DummyAPIException("Parse exception : " + e.getMessage());
        } catch (HttpServerErrorException.InternalServerError exception) {
            throw new DummyAPIException("Message : " + exception.getMessage());
        }
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5000))
    public Long getHighestSalary() {
        final List<Employee> employeeDetails = getEmployeeDetails();
        return employeeDetails.stream()
                .sorted(Comparator.comparingLong(Employee::getEmployee_salary).reversed())
                .limit(1).collect(Collectors.toList()).get(0).getEmployee_salary();
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5000))
    public List<String> getHighestEarningEmployeeList() {
        List<String> employeeNames = new ArrayList<>();

        final List<Employee> employeeDetails = getEmployeeDetails();

        employeeDetails.stream()
                .sorted(Comparator.comparingLong(Employee::getEmployee_salary).reversed())
                .limit(10)
                .forEach(e -> employeeNames.add(e.getEmployee_name()));

        return employeeNames;
    }
}
