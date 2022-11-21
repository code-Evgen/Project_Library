package ru.tatarinov.project2Boot.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tatarinov.project2Boot.model.Person;
import ru.tatarinov.project2Boot.security.PersonDetails;
import ru.tatarinov.project2Boot.services.PeopleService;
import ru.tatarinov.project2Boot.services.RegistrationService;
import ru.tatarinov.project2Boot.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final RegistrationService registrationService;

    public AuthController(PersonValidator personValidator, PeopleService peopleService, RegistrationService registrationService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person, Model model){
        String role = PersonDetails.getRole();
        if (role.equals("ROLE_ADMIN")) {
            model.addAttribute("admin", true);
            model.addAttribute("roleList", peopleService.getRolesList());
        }
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, Model model){
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()){
            String role = PersonDetails.getRole();
            if (role.equals("ROLE_ADMIN")) {
                model.addAttribute("admin", true);
                model.addAttribute("roleList", peopleService.getRolesList());
            }
            return "auth/registration";
        }
        registrationService.register(person);

        Boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if (authenticated)
            return "redirect:/people";
        else
            return "redirect:/auth/login";
    }

    @GetMapping("/access_denied")
    public String access_denied(){
        return "auth/accessDenied";
    }
}
