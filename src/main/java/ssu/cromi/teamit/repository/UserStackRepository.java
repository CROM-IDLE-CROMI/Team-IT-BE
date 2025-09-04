package ssu.cromi.teamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.UserStack;

import java.util.List;

@Repository
public interface UserStackRepository extends JpaRepository<UserStack, Long> {
    List<UserStack> findByUser(User user);
    List<UserStack> findByUserUid(String uid);
    List<UserStack> findByUserUidAndIsRepresentativeTrue(String uid);
}