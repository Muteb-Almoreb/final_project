package org.example.trucksy.Repository;

import org.example.trucksy.Model.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCardRepository extends JpaRepository<BankCard, Integer> {
    BankCard findBankCardById(Integer id);
}
