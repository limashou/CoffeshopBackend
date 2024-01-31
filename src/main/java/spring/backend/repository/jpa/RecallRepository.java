package spring.backend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.entity.DopInfoCoffeeshop;
import spring.backend.entity.Recall;
import spring.backend.entity.User;

import java.util.Optional;

@Repository
public interface RecallRepository extends JpaRepository<Recall,Long> {
     Optional<Recall> findByDopInfoCoffeeshopAndUser(DopInfoCoffeeshop dopInfoCoffeeshop, User user);
}
