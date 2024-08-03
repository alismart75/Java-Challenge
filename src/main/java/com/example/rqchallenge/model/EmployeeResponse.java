package com.example.rqchallenge.model;

import java.util.List;

public class EmployeeResponse {
    private String status;
    private List<Employee> data;

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
        this.data = data;
    }
}