package spring.backend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.entity.Coffeeshop;
import spring.backend.entity.DopInfoCoffeeshop;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface DopInfoRepository extends JpaRepository<DopInfoCoffeeshop,Long> {
    List<DopInfoCoffeeshop> findByCoffeeshop(Coffeeshop coffeeshop);
    List<DopInfoCoffeeshop> findByCity(String city);
    @Query("SELECT d FROM DopInfoCoffeeshop d LEFT JOIN FETCH d.recallList WHERE d.id = :id")
    DopInfoCoffeeshop initializeRecallList(Long id);

}
