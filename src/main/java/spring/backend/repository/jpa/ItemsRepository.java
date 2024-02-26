package spring.backend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.entity.OrderItems;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<OrderItems,Long> {
    List<OrderItems> findByOrderId(Long id);
}
