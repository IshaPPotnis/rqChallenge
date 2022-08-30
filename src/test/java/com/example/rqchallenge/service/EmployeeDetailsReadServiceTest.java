package com.example.rqchallenge.service;

import com.example.rqchallenge.exception.DummyAPIException;
import com.example.rqchallenge.exception.InvalidDataException;
import com.example.rqchallenge.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeDetailsReadServiceTest {

    private EmployeeDetailsReadService employeeDetailsReadService;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JSONParser jsonParser = new JSONParser();

        employeeDetailsReadService = new EmployeeDetailsReadService();

        ReflectionTestUtils.setField(employeeDetailsReadService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(employeeDetailsReadService, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(employeeDetailsReadService, "dummyRestApi", "dummyApi");
        ReflectionTestUtils.setField(employeeDetailsReadService, "jsonParser", jsonParser);
    }

    @Test
    void getEmployeeDetails() {
        final String mockResponse = "{\n" +
                "\"status\": \"success\",\n" +
                "\"data\": [\n" +
                "\t{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"employee_name\": \"Tiger Nixon\",\n" +
                "\t\"employee_salary\": \"320800\",\n" +
                "\t\"employee_age\": \"61\",\n" +
                "\t\"profile_image\": \"\"\n" +
                "\t}\n" +
                "\t]\n" +
                "}";

        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse);

        final List<Employee> employeeDetails = employeeDetailsReadService.getEmployeeDetails();
        Assertions.assertFalse(employeeDetails.isEmpty());
    }

    @Test()
    void getEmployeeDetails_ParseException() {
        final String mockResponse = "Hello";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse);
        try {
            employeeDetailsReadService.getEmployeeDetails();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DummyAPIException);
        }
    }

    @Test()
    void getEmployeeDetails_InternalServerError() {
        when(restTemplate.getForObject(anyString(), any())).thenThrow(HttpServerErrorException.InternalServerError.class);
        try {
            employeeDetailsReadService.getEmployeeDetails();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DummyAPIException);
        }
    }

    @Test
    void getEmployeeByEmployeeId() {
        final String mockResponse = "{\n" +
                "\"status\": \"success\",\n" +
                "\"data\": \n" +
                "\t{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"employee_name\": \"Tiger Nixon\",\n" +
                "\t\"employee_salary\": \"320800\",\n" +
                "\t\"employee_age\": \"61\",\n" +
                "\t\"profile_image\": \"\"\n" +
                "\t}\n" +
                "}";

        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse);

        Assertions.assertNotNull(employeeDetailsReadService.getEmployeeByEmployeeId("1"));
    }

    @Test
    void getEmployeeByEmployeeId_ParseException() {
        final String mockResponse = "Dummy Response";

        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse);

        try {
            employeeDetailsReadService.getEmployeeByEmployeeId("1");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DummyAPIException);
            Assertions.assertTrue(e.getMessage().contains("Parse exception"));
        }
    }

    @Test
    void getEmployeeByEmployeeId_InternalServerError() {

        when(restTemplate.getForObject(anyString(), any())).thenThrow(HttpServerErrorException.InternalServerError.class);

        try {
            employeeDetailsReadService.getEmployeeByEmployeeId("1");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DummyAPIException);
            Assertions.assertTrue(e.getMessage().contains("Message"));
        }
    }

    @Test
    void getEmployeeByEmployeeId_InvalidId_InvalidDataException() {
        try {
            employeeDetailsReadService.getEmployeeByEmployeeId(null);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidDataException);
        }
    }

    @Test
    void getEmployeeByEmployeeId_GivenStringAsId_InvalidDataException() {
        try {
            employeeDetailsReadService.getEmployeeByEmployeeId("Abcd");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidDataException);
        }
    }

    @Test
    void getHighestSalary() {
        final String mockResponse = "{\n" +
                "\"status\": \"success\",\n" +
                "\"data\": [\n" +
                "\t{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"employee_name\": \"Tiger Nixon\",\n" +
                "\t\"employee_salary\": \"320800\",\n" +
                "\t\"employee_age\": \"61\",\n" +
                "\t\"profile_image\": \"\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"employee_name\": \"dummy 2\",\n" +
                "\t\"employee_salary\": \"32090\",\n" +
                "\t\"employee_age\": \"63\",\n" +
                "\t\"profile_image\": \"\"\n" +
                "\t}\n" +
                "\t]\n" +
                "}";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse);

        final Long highestSalary = employeeDetailsReadService.getHighestSalary();

        Assertions.assertEquals(320800, (long) highestSalary);
    }

    @Test
    void getHighestEarningEmployeeList() {
        final String mockResponse = "{\n" +
                "\"status\": \"success\",\n" +
                "\"data\": [\n" +
                "\t{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"employee_name\": \"Tiger Nixon\",\n" +
                "\t\"employee_salary\": \"320800\",\n" +
                "\t\"employee_age\": \"61\",\n" +
                "\t\"profile_image\": \"\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"employee_name\": \"dummy 2\",\n" +
                "\t\"employee_salary\": \"32090\",\n" +
                "\t\"employee_age\": \"63\",\n" +
                "\t\"profile_image\": \"\"\n" +
                "\t}\n" +
                "\t]\n" +
                "}";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse);

        final List<String> highestSalary = employeeDetailsReadService.getHighestEarningEmployeeList();

        Assertions.assertEquals("Tiger Nixon", highestSalary.get(0));
    }
}