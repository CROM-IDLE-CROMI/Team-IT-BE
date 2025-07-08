package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.domain.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUID(String UID);
    boolean existByEmail(String email);
    boolean existsByUID(String UID);
}
