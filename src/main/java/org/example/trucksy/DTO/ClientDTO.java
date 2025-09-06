package org.example.trucksy.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private String username;
    private String email;
    private String password;
    private String phone;
    private String city;
    private String district;
}
