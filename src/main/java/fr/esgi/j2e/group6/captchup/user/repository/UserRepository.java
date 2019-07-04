package fr.esgi.j2e.group6.captchup.user.repository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}
