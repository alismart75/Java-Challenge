package com.example.rqchallenge.controller;

import com.example.rqchallenge.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IEmployeeController {

    @GetMapping()
    ResponseEntity<Map<String, Object>> getAllEmployees() throws IOException;

    @GetMapping("/search/{searchString}")
    ResponseEntity<Map<String, Object>> getEmployeesByNameSearch(@PathVariable String searchString);

    @GetMapping("/{id}")
    ResponseEntity<Map<String, Object>> getEmployeeById(@PathVariable String id);

    @GetMapping("/highestSalary")
    ResponseEntity<Map<String, Object>> getHighestSalaryOfEmployees();

    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<Map<String, Object>> getTopTenHighestEarningEmployeeNames();

    @PostMapping()
    ResponseEntity<Map<String, Object>> createEmployee(@RequestBody Map<String, Object> employeeInput);

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, Object>> deleteEmployeeById(@PathVariable String id);

}
