package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Api.ApiException;
import org.example.trucksy.Model.BankCard;
import org.example.trucksy.Model.User;
import org.example.trucksy.Repository.AuthRepository;
import org.example.trucksy.Repository.BankCardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankCardService {
    private final AuthRepository authRepository;
    private final BankCardRepository bankCardRepository;

    public void addBankCard(Integer userId, BankCard bankCard) {
        User user = authRepository.findUserById(userId);
        if(user == null){
            throw new ApiException("user not found");
        }
        bankCard.setUser(user);
        bankCardRepository.save(bankCard);
        user.setBankCard(bankCard);
        authRepository.save(user);
    }

    public BankCard getBankCardByUserId(Integer userId){
        User user = authRepository.findUserById(userId);
        if(user ==  null){
            throw new ApiException("user not found");
        }
        if(user.getBankCard() == null){
            throw new ApiException("user dose not add his Payment Card");
        }
        return user.getBankCard();
    }
}
