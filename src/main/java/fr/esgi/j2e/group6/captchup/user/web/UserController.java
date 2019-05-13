package fr.esgi.j2e.group6.captchup.user.web;

import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import fr.esgi.j2e.group6.captchup.user.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Controller
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService userService;

    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          UserService userService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path="/{id}")
    public @ResponseBody User getUserById(@PathVariable("id") int id) {
        //return userRepository.findById(id);
        User result = userRepository.findById(id).get();

        System.out.println(result);

        return result;
    }

    @DeleteMapping(path = "/delete/{id}")
    public @ResponseBody void deleteUser(@PathVariable("id") int id) {
        userRepository.deleteById(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path="/sign-up")
    public @ResponseBody User signUp(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PatchMapping(path = "/follow/{id}")
    public ResponseEntity<User> followUser(@PathVariable("id") int id) {
        User user = null;
        try {
            user = userService.followUser(userRepository.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    
}
