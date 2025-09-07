package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ClientRepository clientRepository;

}
