package org.example.trucksy.Repository;

import org.example.trucksy.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<User , Integer> {
    User findByUsername(String username);
    User findUserById(Integer id);

    @Query("select u from User u where u.role = 'OWNER'")
    List<User> findAllUsersWithRoleOwner();

}
