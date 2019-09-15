package com.company;

public class Person {
    private String id_person;
    private String name;
    private String last_name;
    private String name_post;
    private String name_department;

    public Person(String id, String name, String last_name, String name_post, String name_department)
    {
        id_person = id;
        this.name = name;
        this.last_name = last_name;
        this.name_post = name_post;
        this.name_department = name_department;
    }

    public String getId_person() {
        return id_person;
    }

    public String getName(){
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getName_department() {
        return name_department;
    }

    public String getName_post() {
        return name_post;
    }

}
