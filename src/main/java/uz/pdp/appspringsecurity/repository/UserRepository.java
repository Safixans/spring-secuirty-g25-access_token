package uz.pdp.appspringsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appspringsecurity.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
