package com.example.rqchallenge.service;

import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.model.EmployeeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String API_URL = "https://dummy.restapiexample.com/api/v1";

    private <T> Mono<T> handleTooManyRequests(Mono<T> mono) {
        return mono.onErrorResume(WebClientResponseException.TooManyRequests.class, ex -> {
            logger.error("Received 429 Too Many Requests. Please try again later.");
            return Mono.error(new RuntimeException("Too many requests, please try again later."));
        });
    }

    public List<Employee> getAllEmployees() {
        logger.debug("Fetching all employees");
        WebClient webClient = webClientBuilder.baseUrl(API_URL).build();
        Mono<EmployeeResponse> response = handleTooManyRequests(webClient.get()
                .uri("/employees")
                .retrieve()
                .bodyToMono(EmployeeResponse.class));
        List<Employee> employees = response.blockOptional().map(EmployeeResponse::getData).orElseThrow(() -> new RuntimeException("Too many requests"));
        logger.info("Fetched {} employees", employees.size());
        return employees;
    }

    public List<Employee> getEmployeesByNameSearch(String searchString) {
        logger.debug("Searching employees by name: {}", searchString);
        List<Employee> employees = getAllEmployees().stream()
                .filter(employee -> employee.getEmployeeName().contains(searchString))
                .collect(Collectors.toList());
        logger.info("Found {} employees matching name: {}", employees.size(), searchString);
        return employees;
    }

    public Employee getEmployeeById(String id) {
        logger.debug("Fetching employee by id: {}", id);
        WebClient webClient = webClientBuilder.baseUrl(API_URL).build();
        Mono<EmployeeResponse> response = handleTooManyRequests(webClient.get()
                .uri("/employee/{id}", id)
                .retrieve()
                .bodyToMono(EmployeeResponse.class));
        Employee employee = response.blockOptional().map(EmployeeResponse::getData).flatMap(data -> data.stream().findFirst()).orElseThrow(() -> new RuntimeException("Too many requests"));
        logger.info("Fetched employee: {}", employee);
        return employee;
    }

    public Integer getHighestSalaryOfEmployees() {
        logger.debug("Fetching highest salary of employees");
        int highestSalary = getAllEmployees().stream()
                .mapToInt(Employee::getEmployeeSalary)
                .max()
                .orElse(0);
        logger.info("Highest salary of employees: {}", highestSalary);
        return highestSalary;
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        logger.debug("Fetching top 10 highest earning employee names");
        List<String> topTenEmployees = getAllEmployees().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getEmployeeSalary(), e1.getEmployeeSalary()))
                .limit(10)
                .map(Employee::getEmployeeName)
                .collect(Collectors.toList());
        logger.info("Top 10 highest earning employees: {}", topTenEmployees);
        return topTenEmployees;
    }

    public Employee createEmployee(Map<String, Object> employeeInput) {
        logger.debug("Creating new employee with input: {}", employeeInput);
        WebClient webClient = webClientBuilder.baseUrl(API_URL).build();
        Mono<EmployeeResponse> response = handleTooManyRequests(webClient.post()
                .uri("/create")
                .bodyValue(employeeInput)
                .retrieve()
                .bodyToMono(EmployeeResponse.class));
        Employee newEmployee = response.blockOptional().map(EmployeeResponse::getData).flatMap(data -> data.stream().findFirst()).orElseThrow(() -> new RuntimeException("Too many requests"));
        logger.info("Created new employee: {}", newEmployee);
        return newEmployee;
    }

    public String deleteEmployeeById(String id) {
        logger.debug("Deleting employee by id: {}", id);
        WebClient webClient = webClientBuilder.baseUrl(API_URL).build();
        handleTooManyRequests(webClient.delete()
                .uri("/delete/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)).blockOptional().orElseThrow(() -> new RuntimeException("Too many requests"));
        logger.info("Deleted employee with id: {}", id);
        return "Employee with id " + id + " has been deleted";
    }
}