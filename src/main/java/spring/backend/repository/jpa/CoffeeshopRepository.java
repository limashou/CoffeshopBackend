package spring.backend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.entity.Coffeeshop;

@Repository
public interface CoffeeshopRepository extends JpaRepository<Coffeeshop,Long> {

}
