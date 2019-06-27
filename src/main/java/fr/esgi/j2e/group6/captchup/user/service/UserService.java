package fr.esgi.j2e.group6.captchup.user.service;

import com.google.api.client.util.DateTime;
import fr.esgi.j2e.group6.captchup.level.model.Level;
import fr.esgi.j2e.group6.captchup.level.repository.LevelRepository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import fr.esgi.j2e.group6.captchup.user.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

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

        if(!loggedUser.getFollow().contains(userToFollow.get())) {
            loggedUser.getFollow().add(userToFollow.get());
            userRepository.save(loggedUser);
        }

        return loggedUser;
    }

    public User getUserByUsername(@NotNull String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentLoggedInUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return this.getUserByUsername(username);
    }

    public User unfollowUser(User activeUser, Optional<User> userToUnfollow) throws NotFoundException, AccessDeniedException {
        if(!userToUnfollow.isPresent()) {
            throw new NotFoundException("User to unfollow doesn't exists.");
        }

        if(activeUser == null) {
            throw new AccessDeniedException("You need to be connected.");
        }

        if(activeUser.getFollow().contains(userToUnfollow.get())) {
            activeUser.getFollow().remove(userToUnfollow.get());
            userRepository.save(activeUser);
        }

        return activeUser;
    }

    public void deleteUser(int id) throws NotFoundException, AccessDeniedException {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new NotFoundException("User with id '" + id + "' doesn't exist.");
        }

        for (User follower: user.get().getFollowedBy()) {
            unfollowUser(follower, user);
            userRepository.save(follower);
        }

        userRepository.delete(user.get());
    }
}
