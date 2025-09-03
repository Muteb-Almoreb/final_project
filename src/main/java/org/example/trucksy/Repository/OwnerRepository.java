package org.example.trucksy.Repository;

import org.example.trucksy.Model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    Owner findOwnerById(Integer id);
}
