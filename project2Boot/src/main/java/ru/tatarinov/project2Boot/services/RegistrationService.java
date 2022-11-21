package ru.tatarinov.project2Boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tatarinov.project2Boot.model.Person;

@Service
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final PeopleService peopleService;

    @Autowired
    public RegistrationService(PasswordEncoder passwordEncoder, PeopleService peopleService) {
        this.passwordEncoder = passwordEncoder;
        this.peopleService = peopleService;
    }

    public void register(Person person){
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        peopleService.newPerson(person);
    }
}
