package com.example.rqchallenge;

import com.example.rqchallenge.controller.EmployeeController;
import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee("1", "Tiger Nixon", 320800, 61, ""),
                new Employee("2", "Garrett Winters", 170750, 63, "")
        );
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].employee_name", is("Tiger Nixon")))
                .andExpect(jsonPath("$.data[1].employee_name", is("Garrett Winters")));
    }

    @Test
    public void testGetEmployeesByNameSearch() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee("1", "Tiger Nixon", 320800, 61, ""));
        when(employeeService.getEmployeesByNameSearch("Tiger")).thenReturn(employees);

        mockMvc.perform(get("/employees/search/Tiger")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].employee_name", is("Tiger Nixon")));
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        Employee employee = new Employee("1", "Tiger Nixon", 320800, 61, "");
        when(employeeService.getEmployeeById("1")).thenReturn(employee);

        mockMvc.perform(get("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.employee_name", is("Tiger Nixon")))
                .andExpect(jsonPath("$.data.employee_salary", is(320800)))
                .andExpect(jsonPath("$.data.employee_age", is(61)))
                .andExpect(jsonPath("$.data.id", is("1")));
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws Exception {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(320800);

        mockMvc.perform(get("/employees/highestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", is(320800)));
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> topTenEmployees = Arrays.asList("Tiger Nixon", "Garrett Winters");
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(topTenEmployees);

        mockMvc.perform(get("/employees/topTenHighestEarningEmployeeNames")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0]", is("Tiger Nixon")))
                .andExpect(jsonPath("$.data[1]", is("Garrett Winters")));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee("1", "Tiger Nixon", 320800, 61, "");
        when(employeeService.createEmployee(anyMap())).thenReturn(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Tiger Nixon\",\"salary\":\"320800\",\"age\":\"61\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.employee_name", is("Tiger Nixon")))
                .andExpect(jsonPath("$.data.employee_salary", is(320800)))
                .andExpect(jsonPath("$.data.employee_age", is(61)))
                .andExpect(jsonPath("$.data.id", is("1")));
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {
        when(employeeService.deleteEmployeeById("1")).thenReturn("Tiger Nixon");

        mockMvc.perform(delete("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", is("Tiger Nixon")));
    }
}