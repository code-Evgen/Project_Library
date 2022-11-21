package ru.tatarinov.project2Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tatarinov.project2Boot.model.Book;
import ru.tatarinov.project2Boot.model.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);
    Optional<Book> findByName(String name);
    List<Book> findByNameStartingWithIgnoreCase(String name);

    @Modifying
    @Query("UPDATE Book b set b.name = :name, b.author = :author, b.year = :year where b.id = :id")
    void updateBook(@Param("name") String name, @Param("author") String author,  @Param("year") int year, @Param("id") int id);
}
