package spring.backend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.entity.Menu;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
    List<Menu> findTop4ByCoffeeshopIdOrderByIdDesc(Long coffeeshopId);
}
