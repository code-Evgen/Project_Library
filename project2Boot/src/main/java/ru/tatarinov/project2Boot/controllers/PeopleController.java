package ru.tatarinov.project2Boot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tatarinov.project2Boot.model.Person;
import ru.tatarinov.project2Boot.services.BooksService;
import ru.tatarinov.project2Boot.services.PeopleService;
import ru.tatarinov.project2Boot.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/people")
public class PeopleController {
    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public PeopleController(PersonValidator personValidator, PeopleService peopleService, BooksService booksService) {
        this.booksService = booksService;
//        this.bookDAO = bookDAO;
        this.personValidator = personValidator;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", peopleService.getPersonList());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("person", peopleService.showPerson(id));
        model.addAttribute("books", peopleService.getPersonBooks(id));
        return ("people/show");
    }

    @GetMapping("/new")
    public String addPerson(@ModelAttribute("person") Person person){
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/new";
        peopleService.newPerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("person", peopleService.showPerson(id));
        model.addAttribute("basePerson", model.getAttribute("person"));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String saveChange(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                             @ModelAttribute("basePerson") Person basePerson){
        if (!person.getUsername().equals(basePerson.getUsername()))
            personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/edit";
        peopleService.edit(person);
        return "redirect:/people/" + person.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        peopleService.delete(person);
        return "redirect:/people";
    }
}
