package ssu.cromi.teamit.repository.postboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssu.cromi.teamit.entity.postboard.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}