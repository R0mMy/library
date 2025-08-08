package org.example.models;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class Person {
    private int id;

    @NotNull(message = "Имя не может быть пустым")
    @Size(min = 5, max = 100, message = "Имя должно быть не менее 5, не больше 100 символов")
    private String name;

    @NotNull(message = "Год не может быть пустым")
    @Min(value = 1910, message = "Год не может быть раньше 1910")
    private int yearOfBirth;

    public Person() {
    }

    private List<Book> books;

    public Person(String name, int yearOfBirth, List<Book> books) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
