package ssu.cromi.teamit.repository.postboard;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.postboard.PostScrap;
import ssu.cromi.teamit.entity.postboard.PostScrapId;

public interface PostScrapRepository extends JpaRepository<PostScrap, PostScrapId> {
}