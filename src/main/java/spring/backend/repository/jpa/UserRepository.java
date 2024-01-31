package spring.backend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.backend.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT DISTINCT oc FROM User oc JOIN FETCH oc.roles WHERE oc.username =:username")
    User findByUsername(String username);
    @Query("SELECT DISTINCT oc FROM User oc JOIN FETCH oc.roles WHERE oc.email =:email")
    User findByEmail(String email);
    @Query("SELECT DISTINCT oc FROM User oc JOIN FETCH oc.roles")
    List<User> findWithRole();

    @Query("SELECT DISTINCT oc FROM User oc JOIN FETCH oc.roles WHERE oc.id =:id")
    User findByIdWithRole(long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
