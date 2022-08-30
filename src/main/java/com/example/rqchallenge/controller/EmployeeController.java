package com.example.rqchallenge.controller;

import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.exception.DummyAPIException;
import com.example.rqchallenge.exception.InvalidDataException;
import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.service.EmployeeDetailsReadService;
import com.example.rqchallenge.service.EmployeeDetailsWriteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Log4j2
public class EmployeeController implements IEmployeeController {

    @Autowired
    EmployeeDetailsReadService employeeDetailsReadService;
    @Autowired
    EmployeeDetailsWriteService employeeDetailsWriteService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        final List<Employee> employeeDetails = employeeDetailsReadService.getEmployeeDetails();
        return new ResponseEntity<>(employeeDetails, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(final String searchString) {
        try {
            final List<Employee> employeeByName = employeeDetailsReadService.getEmployeeDetails().stream().filter(e -> e.getEmployee_name().equals(searchString)).collect(Collectors.toList());
            if (employeeByName.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(employeeByName, HttpStatus.OK);
            }
        } catch (DummyAPIException e) {
            throw new DummyAPIException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(final String id) {
        final Employee employeeByEmployeeId = employeeDetailsReadService.getEmployeeByEmployeeId(id);
        if (employeeByEmployeeId == null) {
            throw new DummyAPIException("No Records for " + id + " found");
        }
        return new ResponseEntity<>(employeeByEmployeeId, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        final Integer highestSalary = Math.toIntExact(employeeDetailsReadService.getHighestSalary());
        return new ResponseEntity<>(highestSalary, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return new ResponseEntity<>(employeeDetailsReadService.getHighestEarningEmployeeList(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(final Map<String, Object> employeeInput) {
        return new ResponseEntity<>(employeeDetailsWriteService.insertEmployeeData(employeeInput), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(final String id) {
        employeeDetailsWriteService.deleteEmployeeDataById(id);
        return new ResponseEntity<>("Successfully deleted - " + id, HttpStatus.OK);
    }
}
