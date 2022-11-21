package ru.tatarinov.project2Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tatarinov.project2Boot.model.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> getPersonByUsername(String username);

    @Modifying
    @Query("UPDATE Person p set p.username = :username, p.year = :year where p.id = :id")
    void updatePerson(@Param("username") String username, @Param("year") int year, @Param("id") int id);
}
