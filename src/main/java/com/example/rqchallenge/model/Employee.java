package com.example.rqchallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Employee {
    public Long id;
    public String employee_name;
    public Long employee_salary;
    public Long employee_age;
    @JsonIgnore
    public String profile_image;
}
