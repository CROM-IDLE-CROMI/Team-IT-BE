package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.domain.Stack;
import java.util.Optional;

public interface StackRepository extends JpaRepository<Stack, Integer> {
    Optional<Stack> findByTag(String tag);
}
