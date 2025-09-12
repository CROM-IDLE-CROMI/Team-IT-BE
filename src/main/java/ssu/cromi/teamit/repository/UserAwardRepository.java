package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.UserAward;

import java.util.List;

@Repository
public interface UserAwardRepository extends JpaRepository<UserAward, Long> {
    List<UserAward> findByUser(User user);
    List<UserAward> findByUserUid(String uid);
}