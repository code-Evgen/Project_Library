package ru.tatarinov.project2Boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tatarinov.project2Boot.services.PeopleDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PeopleDetailsService peopleDetailsService;

    public SecurityConfig(PeopleDetailsService peopleDetailsService) {
        this.peopleDetailsService = peopleDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                //.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/auth/login", "/auth/registration", "/error", "/auth/logout").permitAll()
                    .antMatchers("/books", "/books/search", "/books/{id:\\d+}").hasAnyRole("ADMIN", "USER")
                    .antMatchers("/people", "/people/**", "/books/**").hasRole("ADMIN")
                    .anyRequest().hasAnyRole("ADMIN", "USER")
                .and()
                    .formLogin().loginPage("/auth/login")
                    .loginProcessingUrl("/process_login")
                    .defaultSuccessUrl("/books", true)
                    .failureUrl("/auth/login?error")
                .and()
                    .logout()
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/auth/login")
                .and()
                    .exceptionHandling().accessDeniedPage("/auth/access_denied")
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(peopleDetailsService);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        //return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }
}
