package spring.backend.repository.paging;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import spring.backend.entity.DopInfoCoffeeshop;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface DopInfoRepositoryPaging extends PagingAndSortingRepository<DopInfoCoffeeshop,Integer> {
    List<DopInfoCoffeeshop> findAllByCity(String city, Pageable pageable);
}
