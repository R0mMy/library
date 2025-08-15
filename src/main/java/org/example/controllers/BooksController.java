package org.example.controllers;

import org.example.models.Book;
import org.example.models.Person;
import org.example.services.BookService;
import org.example.services.PersonService;
import org.example.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final PersonService personService;
    private final BookService bookService;

    private final BookValidator bookValidator;
    @Autowired
    public BooksController(PersonService personService, BookService bookService, BookValidator bookValidator) {
        this.personService = personService;
        this.bookService = bookService;
        this.bookValidator = bookValidator;
    }

    @GetMapping
    public String index(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "10") int size,
                        @RequestParam(name = "sort", defaultValue = "false") boolean sort){
        model.addAttribute("books", bookService.findAll(page, size, sort));

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("books", bookService.findOne(id));
        Optional<Person> bookOwner = bookService.getBookOwner(id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", personService.findAll());

        return "books/show";
    }
    @GetMapping("/new")
    public String newBook(@ModelAttribute("books")  Book book){
        return "books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("books") @Valid Book book,
                         BindingResult bindingResult){
        bookValidator.validate(book, bindingResult);
        if(bindingResult.hasErrors())
            return "books/edit";
        bookService.save(book);
        return "redirect:/books";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("books", bookService.findOne(id));
        return "/books/edit";
    }
    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("books") @Valid Book book,
                         BindingResult bindingResult ){
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "/books/edit";
        bookService.update(id, book);

        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookService.release(id);
        return "redirect:/books/" + id;

    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        bookService.assign(id, person);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage(){
        return "books/search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("query") String query){
        model.addAttribute("books", bookService.search(query));
        return "books/search";
    }








}
