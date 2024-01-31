package spring.backend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.backend.entity.SpecialOffer;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<SpecialOffer,Long> {
    @Query("SELECT ob FROM SpecialOffer ob LEFT JOIN FETCH ob.itemsList m WHERE ob.coffeeshop.id = :id")
    List<SpecialOffer> findAllByCoffeeshopId(Long id);
}
