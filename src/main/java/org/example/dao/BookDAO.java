package org.example.dao;

import org.example.models.Book;
import org.example.models.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index(){
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Optional<Book> show(String title){
        return jdbcTemplate.query("SELECT * FROM Book WHERE title=?",
                        new Object[]{title}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny();
    }

    public Book show(int id){
        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book){
        jdbcTemplate.update("INSERT INTO Book( title, author, year_of_publication) VALUES(?, ?, ?)",
                 book.getTitle(), book.getAuthor(), book.getYearOfPublication());
    }

    public void update(int id, Book book){
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year_of_publication=? WHERE id =?",
                book.getTitle(), book.getAuthor(), book.getYearOfPublication(), id);
    }

    public  void delete (int  id){
        jdbcTemplate.update("DELETE FROM Book WHERE id =?", id);
    }

    public Optional<Person> getBookOwner(int id){
        return jdbcTemplate.query("SELECT Person.* FROM Person JOIN Book ON Book.person_id = Person.id WHERE Book.id=?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }



    public void release(int id) {
        jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE id=?", id);
    }

    public void assign(int id, Person person) {
        jdbcTemplate.update("UPDATE Book SET person_id=? where id=?", person.getId(), id);
    }
}
