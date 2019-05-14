package fr.esgi.j2e.group6.captchup.user.service;

import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User applicationUser = userRepository.findByUsername(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getUsername(), applicationUser.getPassword());
    }

    public User followUser(Optional<User> userToFollow) throws NotFoundException, AccessDeniedException, IllegalArgumentException {
        User loggedUser = getCurrentLoggedInUser();

        if(!userToFollow.isPresent()) {
            throw new NotFoundException("User to follow doesn't exists.");
        }

        if(loggedUser == null) {
            throw new AccessDeniedException("You need to be connected.");
        }

        if(loggedUser == userToFollow.get()) {
            throw new IllegalArgumentException();
        }

        if(!loggedUser.getFollowed().contains(userToFollow.get())) {
            loggedUser.getFollowed().add(userToFollow.get());
            userRepository.save(loggedUser);
        }

        return loggedUser;
    }

    public User getCurrentLoggedInUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(username);
    }

    public User unfollowUser(Optional<User> userToUnfollow) throws NotFoundException, AccessDeniedException {
        User loggedUser = getCurrentLoggedInUser();

        if(!userToUnfollow.isPresent()) {
            throw new NotFoundException("User to unfollow doesn't exists.");
        }

        if(loggedUser == null) {
            throw new AccessDeniedException("You need to be connected.");
        }

        if(loggedUser.getFollowed().contains(userToUnfollow.get())) {
            loggedUser.getFollowed().remove(userToUnfollow.get());
            userRepository.save(loggedUser);
        }

        return loggedUser;
    }
}
