package ru.tatarinov.project2Boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.project2Boot.model.Book;
import ru.tatarinov.project2Boot.model.Person;
import ru.tatarinov.project2Boot.repositories.BooksRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> getBookList(Boolean sort){
        if (sort == true)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll(Sort.by("name"));
    }

    public List<Book> getBookList(Integer page, Integer booksPerPage, Boolean sort){
        if (sort == true)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book showBook(int id){
        return booksRepository.findById(id).orElse(null);
    }

    public Person getBookOwner(int id){
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent())
            return book.get().getOwner();
        else
            return null;
    }

    @Transactional
    public void edit(Book book){
        booksRepository.updateBook(book.getName(), book.getAuthor(), book.getYear(), book.getId());
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    @Transactional
    public void newBook(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void assignBook(int id, Person person){
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent()){
            book.get().setOwner(person);
            book.get().setTakenAt(new Date());
        }
    }

    @Transactional
    public void releaseBook(int id){
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent()){
            book.get().setOwner(null);
            book.get().setTakenAt(null);
        }
    }

    public Optional<Book> getBookByName(String name){
        return booksRepository.findByName(name);
    }

    public List<Book> searchBookByName(String name){
        return booksRepository.findByNameStartingWithIgnoreCase(name);
    }
}
