package org.example.services;

import org.example.models.Book;
import org.example.models.Person;
import org.example.repositories.BookRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;

    }
    public List<Book> findAll(int page, int size, boolean sort){
        if (sort == false)
        return bookRepository.findAll(PageRequest.of(page, size)).toList();
        else
            return bookRepository.findAll(PageRequest.of(page, size, Sort.by("yearOfPublication"))).getContent();
    }

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Optional<Book> findOneByTitle(String title){
        return bookRepository.findOneByTitle(title);
    }

    public Book findOne(int id){
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book){
    bookRepository.save(book);
    }
    @Transactional
    public void update(int id, Book book){
        book.setId(id);
        bookRepository.save(book);
    }

    @Transactional
    public  void delete (int  id){
        bookRepository.deleteById(id);

    }

    public Optional<Person> getBookOwner(int id){
        return Optional.ofNullable(bookRepository.findById(id).orElse(null).getOwner());
    }

    @Transactional
    public void release(int id) {
        bookRepository.findById(id).ifPresent(book -> book.setOwner(null));
    }
    @Transactional
    public void assign(int id, Person person) {
        bookRepository.findById(id).get().setTakenAt(Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
      bookRepository.findById(id).ifPresent(book -> book.setOwner(person));
    }

    @Transactional
    public List<Book> search(String name){
       return bookRepository.searchBooksByTitleStartingWith(name);
    }



}
