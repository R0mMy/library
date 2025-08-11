package org.example.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Book {
    private int id;

    @NotNull(message = "Название книги не может быть пустым")
    @Size(min = 1, max = 100, message = "Название книги не может быть больше 100 символов")
    private String title;

    @NotNull(message = "Имя автора не может быть пустым")
    @Size(min = 5, max = 100, message = "Имя Автора не может быть меньше 5, больше 100 символов")

    private String author;

    @NotNull(message = "Год не может быть пустым")
    @Max(value = 2025, message = "Год выпуска не позднее 2025")
    private int yearOfPublication;

    public Book(String title, String author, int yearOfPublication) {

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
