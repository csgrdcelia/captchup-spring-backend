package fr.esgi.j2e.group6.captchup.user.repository;
import fr.esgi.j2e.group6.captchup.user.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
