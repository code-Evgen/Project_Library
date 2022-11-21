package ru.tatarinov.project2Boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.tatarinov.project2Boot.model.Person;
import ru.tatarinov.project2Boot.services.PeopleService;

//import ru.tatarinov.project2Boot.DAO.PersonDAO;

@Component
public class PersonValidator implements Validator {
    private PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person)target;
        if (peopleService.getPersonByName(person.getUsername()).isPresent())
            errors.rejectValue("username", "", "Такой человек уже есть");

    }
}
