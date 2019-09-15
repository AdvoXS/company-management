package com.company;
import java.sql.*;
import java.util.ArrayList;

public class DataBaseControl {
    //DB Connection properties
    private static final String root = System.getProperty("user.dir");//get work directory
    private static final String DB_URL = "jdbc:firebirdsql://localhost:3050/"+root+"/src/db/COMPANY.FDB?charSet=utf-8"; //relative path
    //  "jdbc:firebirdsql://localhost:3050/c:/DataBase/COMPANY.FDB" //absolute path

    //data lists
    private static ArrayList<Person> personsList = null; //list data persons
    private static ArrayList<Post> postsList = null; //list data posts
    private static ArrayList<Department> departmentsList = null; //list data departments

    private static Connection connection;
    public DataBaseControl() throws SQLException
    {
        connection = DriverManager.getConnection(
                DB_URL,
                "SYSDBA", "masterkey");
    }

    //PERSON SQL
    public void changePersonSQL(String id, String name, String last_name, String post, String department) throws  SQLException
    {
        String setPersonInfoStringSQL = "UPDATE person SET name=?, last_name=?, name_post=?, name_department=? where id_person=?";
        PreparedStatement statement = connection.prepareStatement(setPersonInfoStringSQL);
        statement.setString(1, name);
        statement.setString(2, last_name);
        statement.setString(3, post);
        statement.setString(4, department);
        statement.setString(5, id);
        statement.executeUpdate();

    }
    public void deletePersonSQL(String id) throws SQLException
    {
        String deletePersonInfoStringSQL = "DELETE from person where id_person=?";
        PreparedStatement statement = connection.prepareStatement(deletePersonInfoStringSQL);
        statement.setString(1, id);
            statement.execute();

    }
    public void addPersonSQL(String id, String name, String last_name, String post, String department) throws  SQLException
    {

            String addPersonInfoStringSQL = "INSERT INTO person VALUES (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(addPersonInfoStringSQL);

            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, last_name);
            statement.setString(4,post);
            statement.setString(5,department);
            statement.execute();
    }
    public void getAllPersonsSQL() throws SQLException
    {
            if(personsList==null){personsList = new ArrayList<>();}

            String getPersonsInfoStringSQL = "Select * from person";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(getPersonsInfoStringSQL);
            personsList.clear();
            while (result.next()) {
                personsList.add(new Person(result.getString("id_person"), result.getString("name"), result.getString("last_name"),
                        result.getString("name_post"), result.getString("name_department"))); //создание объекта нового работника
            }


    }
    public void getAllPostsSQL() throws SQLException
    {
        if(postsList==null){postsList = new ArrayList<>();}

        String getPostsInfoStringSQL = "Select * from post";
        Statement stat = connection.createStatement();
        ResultSet result = stat.executeQuery(getPostsInfoStringSQL);
        postsList.clear();
        while (result.next()) {
            postsList.add(new Post(result.getString("name_post"), result.getInt("salary"))); //создание объекта должности
        }
    }
    public void changePostSQL(String name_post, int salary) throws  SQLException
    {
        String setPostInfoStringSQL = "UPDATE post SET  salary=? where name_post=?";
        PreparedStatement statement = connection.prepareStatement(setPostInfoStringSQL);
        statement.setInt(1, salary);
        statement.setString(2, name_post);
        statement.executeUpdate();

    }
    public void deletePostSQL(String name_post) throws SQLException
    {
        String deletePostInfoStringSQL = "DELETE from post where name_post=?";
        PreparedStatement statement = connection.prepareStatement(deletePostInfoStringSQL);
        statement.setString(1, name_post);
        statement.execute();

    }
    public void addPostSQL(String name_post, int salary) throws  SQLException
    {
        String addPostInfoStringSQL = "INSERT INTO post VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(addPostInfoStringSQL);
        statement.setString(1, name_post);
        statement.setInt(2, salary);
        statement.execute();
    }
    public void getAllDepartmentsSQL() throws SQLException
    {
        if(departmentsList==null){departmentsList = new ArrayList<>();}

        String getDepartmentsInfoStringSQL = "Select * from department";
        Statement stat = connection.createStatement();
        ResultSet result = stat.executeQuery(getDepartmentsInfoStringSQL);
        departmentsList.clear();
        while (result.next()) {
            departmentsList.add(new Department(result.getString("name_department"), result.getLong("number_phone"),result.getString("email"))); //создание объекта отдела
        }
    }
    public void changeDepartmentSQL(String name_department, long number_phone, String email) throws  SQLException
    {
        String setDepartmentInfoStringSQL = "UPDATE department SET number_phone=?, email=? where name_department=?";
        PreparedStatement statement = connection.prepareStatement(setDepartmentInfoStringSQL);
        statement.setLong(1, number_phone);
        statement.setString(2, email);
        statement.setString(3, name_department);
        statement.executeUpdate();
    }
    public void deleteDepartmentSQL(String name_department) throws SQLException
    {
        String deleteDepartmentInfoStringSQL = "DELETE from department where name_department=?";
        PreparedStatement statement = connection.prepareStatement(deleteDepartmentInfoStringSQL);
        statement.setString(1, name_department);
        statement.execute();
    }
    public void addDepartmentSQL(String name_department, long number_phone, String email) throws  SQLException
    {
        String addDepartmentInfoStringSQL = "INSERT INTO Department VALUES (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(addDepartmentInfoStringSQL);

        statement.setString(1, name_department);
        statement.setLong(2, number_phone);
        statement.setString(3, email);
        statement.execute();
    }


    public  ArrayList<Person> getPersonsList(){
        return personsList;
    }

    public  ArrayList<Department> getDepartmentsList() { return departmentsList; }

    public  ArrayList<Post> getPostsList() { return postsList; }
}
