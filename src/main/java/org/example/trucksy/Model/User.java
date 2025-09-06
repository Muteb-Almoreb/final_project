package org.example.trucksy.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User  implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotEmpty(message = "Username must not be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Column(columnDefinition = "varchar(20) not null unique")
    private String username;


    @NotEmpty(message = "Password must not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(columnDefinition = "varchar(100) not null")
    private String password;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    @Column(columnDefinition = "varchar(50) not null unique")
    private String email;


    @NotEmpty(message = "Phone number must not be empty")
    @Pattern(
            regexp = "^9665[0-9]{8}$",
            message = "Phone number must start with 9665 and be 12 digits (e.g., 9665XXXXXXXX)"
    )
    @Column(columnDefinition = "varchar(12) not null unique")
    private String phoneNumber;


    @NotEmpty(message = "Role must not be empty")
    @Pattern(regexp = "^(CLIENT|OWNER|ADMIN)$", message = "Role must be CLIENT, OWNER, or ADMIN")
    @Column(columnDefinition = "varchar(10) not null")
    private String role;//CLIENT , OWNER , ADMIN


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Owner owner;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Client client;

    @OneToOne(cascade = CascadeType.ALL , mappedBy = "user")
    @PrimaryKeyJoinColumn
    private BankCard bankCard;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
