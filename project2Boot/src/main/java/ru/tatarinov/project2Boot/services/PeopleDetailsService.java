package ru.tatarinov.project2Boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.tatarinov.project2Boot.model.Person;
import ru.tatarinov.project2Boot.security.PersonDetails;

import java.util.Optional;

@Service
public class PeopleDetailsService implements UserDetailsService {
    private final PeopleService peopleService;

    @Autowired
    public PeopleDetailsService(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person= peopleService.getPersonByName(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(person.get());
    }
}
