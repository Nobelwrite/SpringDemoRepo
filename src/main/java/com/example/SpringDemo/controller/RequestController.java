package com.example.SpringDemo.controller;

import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

@RestController
@RequestMapping("/request/handler")
public class RequestController {

    @GetMapping("/greeting")
    public String getGreeting(){
        return "<h1>Hello, User</h1>"
                + "<h3>you are welcome to my endpoint</h3>";
    }
    public String capitalize(String greeting) {
        return capitalize(getGreeting());
    }
    @GetMapping("/persons")
    public List<Person> getAll(){
        List<Person> list = new ArrayList<>();
        try(Connection connection = getConnection("jdbc:mysql://localhost:3306/ingryd", "root",  "Damibllionaire1)");
            Statement statement = connection.createStatement()){
            ResultSet resultSet= statement.executeQuery("SELECT * FROM student");
            while( resultSet.next() ){
                list.add(new Person(resultSet.getString("studentName"), Integer.parseInt(resultSet.getString("regNo")), resultSet.getString("department"), resultSet.getString("enrollmentYear")) );        }
            resultSet.close();
            return list;    }
        catch( SQLException exception ){
            System.out.println(exception.getMessage());
            return new ArrayList<>();    }
    }
    @PostMapping("/persons")
    public boolean postPerson(@RequestBody Person person) {
        try (Connection connection = getConnection("jdbc:mysql://localhost:3306/ingryd", "root", "Damibllionaire1")) {
            String query = String.format("INSERT INTO student(studentName, regNo, department, enrollmentYear) " +
                            "VALUES('%s', %d, '%s', '%s')",
                    person.getName(), person.getAge(), person.getDepartment(), person.getEnrollmentYear());

            Statement statement = connection.createStatement();
            // int rowsAffected = statement.executeUpdate(query);
            statement.execute(query);

            // return rowsAffected > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
        return false;
    }
    @GetMapping("/greeting/greetings")
    public List<String> getAllGreetings() throws IOException {
        String line;
        List<String> postedGreetings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("/Users/user/Desktop/SpringDemo/src/main/java/com/example/SpringDemo/Controller/Greetings.txt")))){
            while ((line = br.readLine()) != null){
                postedGreetings.add(line);
            }
            return postedGreetings;
        }catch (IOException exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @PostMapping("/post-greeting")
    public String postGreeting(@RequestBody String greeting) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> existingGreetings = getAllGreetings();
        for (String st: existingGreetings){
            stringBuilder.append(st);
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/Users/mac/Desktop/SpringDemo/src/main/java/com/example/SpringDemo/controller/greetings.text")))){
            stringBuilder.append(greeting).append("\n");
            stringBuilder.append(capitalize(greeting));
            bw.write(stringBuilder.toString());
            return "Greeting successfully posted";
        }catch (IOException exception){
            System.out.println(exception.getMessage());
        }
        return "Greeting successfully posted";
    }
}
class Person{
    private String name;
    private int age;
    private String department;
    private String enrollmentYear;
    public Person(String name, int age, String department, String enrollmentYear) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.enrollmentYear = enrollmentYear;    }
    public String getName() {
        return name;    }
    public void setName(String name) {
        this.name = name;    }
    public int getAge() {
        return age;    }
    public void setAge(int age) {
        this.age = age;    }
    public String getDepartment() {
        return department;    }
    public void setDepartment(String department) {
        this.department = department;    }
    public String getEnrollmentYear() {
        return enrollmentYear;    }
    public void setEnrollmentYear(String enrollmentYear) {
        this.enrollmentYear = enrollmentYear;    }
}