package com.company;

public class Post {
    private String name_post;
    private int salary;
    public Post(String name_post, int salary)
    {
        this.name_post = name_post;
        this.salary = salary;
    }
    public String getName_post() {
        return name_post;
    }

    public int getSalary() {
        return salary;
    }
}
