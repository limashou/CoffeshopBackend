package spring.backend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.backend.entity.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long user_id);
    @Query("SELECT i FROM Order  i LEFT JOIN FETCH i.items WHERE i.id = :id")
    Optional<Order> findWithItems(Long id);
}
