package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Model.User;
import org.example.trucksy.Repository.AuthRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    // this is for admin

    public List<User> getAllUsers() {
        return authRepository.findAll();
    }
}
