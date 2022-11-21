package ru.tatarinov.project2Boot.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.project2Boot.model.Book;
import ru.tatarinov.project2Boot.model.Person;
import ru.tatarinov.project2Boot.model.Roles;
import ru.tatarinov.project2Boot.repositories.PeopleRepository;

import java.sql.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> getPersonList(){
        return peopleRepository.findAll(Sort.by("username"));
    }

    public Person showPerson(int id){
        return peopleRepository.findById(id).orElse(null);
    }

    public Optional<Person> getPersonByName(String name){
        return peopleRepository.getPersonByUsername(name);
    }

    @Transactional
    public void newPerson(Person person){
        if ( person.getRole() == null )
            person.setRole("ROLE_USER");
        peopleRepository.save(person);
    }

    @Transactional
    public void edit(Person person){
        //peopleRepository.save(person);
        peopleRepository.updatePerson(person.getUsername(), person.getYear(), person.getId());
    }

    @Transactional
    public void delete(Person person){
        peopleRepository.delete(person);
    }

    public List<Book> getPersonBooks(int id){
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            List<Book> books = person.get().getBooks();
            TimeUnit time = TimeUnit.DAYS;
            for (Book book:books){
                long difference = time.convert(new Date().getTime() - book.getTakenAt().getTime(), TimeUnit.MILLISECONDS);
                if ( difference >= 10 )
                    book.setExpired(true);
            }
            return person.get().getBooks();
        }
        else
            return Collections.emptyList();
    }

    public List<String> getRolesList(){
        return Arrays.stream(Roles.values()).map(Roles::name).collect(Collectors.toList());
        //return peopleRepository.getRoles();
    }

}
