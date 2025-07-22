package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.domain.Organization;
import java.util.Optional;

public interface OragnizationRepository extends JpaRepository<Organization,Integer> {
    Optional<Organization> findByName(String name);
}
