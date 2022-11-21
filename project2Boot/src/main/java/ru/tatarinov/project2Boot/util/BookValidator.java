package ru.tatarinov.project2Boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.tatarinov.project2Boot.model.Book;
import ru.tatarinov.project2Boot.services.BooksService;

//import ru.tatarinov.project2Boot.DAO.BookDAO;

@Component
public class BookValidator implements Validator {
//    BookDAO bookDAO;
    BooksService booksService;

    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book)target;
        if (booksService.getBookByName(book.getName()).isPresent())
            errors.rejectValue("name", "","Книга уже существует");
    }
}
