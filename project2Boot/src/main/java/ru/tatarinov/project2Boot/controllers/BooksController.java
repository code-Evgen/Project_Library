package ru.tatarinov.project2Boot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tatarinov.project2Boot.model.Book;
import ru.tatarinov.project2Boot.model.Person;
import ru.tatarinov.project2Boot.security.PersonDetails;
import ru.tatarinov.project2Boot.services.BooksService;
import ru.tatarinov.project2Boot.services.PeopleService;
import ru.tatarinov.project2Boot.util.BookValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookValidator bookValidator;
    private final PeopleService peopleService;
    private final BooksService booksService;

    public BooksController(BookValidator bookValidator, PeopleService peopleService, BooksService booksService) {
        this.bookValidator = bookValidator;
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sort){
        if (page == null || booksPerPage == null)
            model.addAttribute("books", booksService.getBookList(sort));
        else
            model.addAttribute("books", booksService.getBookList(page, booksPerPage, sort));
        return ("books/index");
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", booksService.showBook(id));

        Person bookOwner = booksService.getBookOwner(id);
        if (bookOwner != null)
            model.addAttribute("bookOwner", bookOwner);
        else
            model.addAttribute("people", peopleService.getPersonList());
        String role = PersonDetails.getRole();
        if (role.equals("ROLE_ADMIN"))
            model.addAttribute("admin", true);
        return ("books/show");
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.showBook(id));
        model.addAttribute("baseBook", model.getAttribute("book"));
        return ("books/edit");
    }

    @PatchMapping("/{id}")
    public String saveChange(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                             @ModelAttribute("baseBook") Book baseBook){
        if (!book.getName().equals(baseBook.getName()))
            bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return ("books/edit");
        booksService.edit(book);
        return ("redirect:/books");
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id, @ModelAttribute("book") Book book){
        booksService.delete(id);
        return ("redirect:/books");
    }

    @GetMapping("/new")
    public String addBook(@ModelAttribute("book") Book book){
        return ("books/new");
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return ("books/new");
        booksService.newBook(book);
        return ("redirect:/books");
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        booksService.assignBook(id, person);
        return ("redirect:/books/" + id);
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        booksService.releaseBook(id);
        return ("redirect:/books/" + id);
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "search_book", required = false) String searchBook,
                         Model model){
        model.addAttribute("books", booksService.searchBookByName(searchBook));
        String role = PersonDetails.getRole();
        if (role.equals("ROLE_ADMIN")) {
            model.addAttribute("admin", true);
        }
        return "books/search";
    }
}
