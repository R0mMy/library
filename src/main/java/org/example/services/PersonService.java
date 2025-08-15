package org.example.services;

import org.example.models.Book;
import org.example.models.Person;
import org.example.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }


    public Page<Person> findAll(int page, int size ){
        return personRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Person> findByName(String name){
        return personRepository.findByName(name);
    }

    public  Person findOne(int id){
        return personRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person){
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person){
        person.setId(id);
        personRepository.save(person);
    }

    @Transactional
    public  void delete (int  id){
        personRepository.deleteById(id);
    }

    public List<Book> getBooksByPersonId(int id){


       Optional<Person> person = personRepository.findById(id);
       if (person.isPresent()){
           Hibernate.initialize(person.get().getBooks());

           for(Book book : person.get().getBooks())
           {
               if ( Math.abs(book.getTakenAt().getTime() - new Date().getTime()) > 864000000)
                   book.setExpired(true);
           }



           return person.get().getBooks();
       }

       else
           return Collections.emptyList();
    }

}
