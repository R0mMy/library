package org.example.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Book {
    private int id;

    private int person_id;
    @NotNull(message = "Название книги не может быть пустым")
    @Size(min = 1, max = 100, message = "Название книги не может быть больше 100 символов")
    private String title;

    @NotNull(message = "Имя автора не может быть пустым")
    @Size(min = 5, max = 100, message = "Имя Автора не может быть меньше 5, больше 100 символов")
    private String author;

    @NotNull(message = "Год не может быть пустым")
    private int yearOfPublication;

    public Book(int person_id, String title, String author, int yearOfPublication) {
        this.person_id = person_id;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }
}
