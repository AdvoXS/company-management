package com.company;

public class Department {
    private String name_department;
    private long number_phone;
    private String email;
    public Department(String name_department, long number_phone, String email){
        this.name_department=name_department;
        this.number_phone=number_phone;
        this.email = email;
    }

    public String getName_department() {
        return name_department;
    }

    public long getNumber_phone() {
        return number_phone;
    }

    public String getEmail() {
        return email;
    }
}
