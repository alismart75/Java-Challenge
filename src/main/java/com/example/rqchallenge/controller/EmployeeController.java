package com.example.rqchallenge.controller;

import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController implements IEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Override
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEmployees() throws IOException {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(Map.of("status", "success", "data", employees));
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<Map<String, Object>> getEmployeesByNameSearch(@PathVariable String searchString) {
        List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);
        return ResponseEntity.ok(Map.of("status", "success", "data", employees));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(Map.of("status", "success", "data", employee));
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Map<String, Object>> getHighestSalaryOfEmployees() {
        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
        return ResponseEntity.ok(Map.of("status", "success", "data", highestSalary));
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<Map<String, Object>> getTopTenHighestEarningEmployeeNames() {
        List<String> topTenEmployees = employeeService.getTopTenHighestEarningEmployeeNames();
        return ResponseEntity.ok(Map.of("status", "success", "data", topTenEmployees));
    }

    @Override
    @PostMapping
    public ResponseEntity<Map<String, Object>> createEmployee(@RequestBody Map<String, Object> employeeInput) {
        Employee newEmployee = employeeService.createEmployee(employeeInput);
        return ResponseEntity.ok(Map.of("status", "success", "data", newEmployee));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEmployeeById(@PathVariable String id) {
        String deletedEmployeeName = employeeService.deleteEmployeeById(id);
        return ResponseEntity.ok(Map.of("status", "success", "data", deletedEmployeeName));
    }
}