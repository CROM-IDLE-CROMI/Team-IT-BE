package ssu.cromi.teamit.repository.postboard;

import org.springframework.data.jpa.repository.JpaRepository;
import ssu.cromi.teamit.entity.postboard.Scrap;
import ssu.cromi.teamit.entity.postboard.ScrapId;

public interface ScrapRepository extends JpaRepository<Scrap, ScrapId> {
}