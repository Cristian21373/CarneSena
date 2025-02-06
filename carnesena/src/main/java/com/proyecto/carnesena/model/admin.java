package com.proyecto.carnesena.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Admin")
public class Admin implements UserDetails{
    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    // @Column(name = "idUser", nullable = false, length = 36)
    // private String idUser;

    // @Column(name = "nombres", nullable = false, length = 100)
    // String nombres;

    // @Column(name = "tipo_documento", nullable = false, length = 100)
    // String tipo_documento;

    // @Column(name = "numero_documento", nullable = false, length = 100)
    // String numero_documento;

    // @Column(name = "tipo_sangre", nullable = false, length = 100)
    // String tipo_sangre;

    // @Column(name = "nis", nullable = false, length = 100)
    // String nis;

    // @Column(name = "imagen", nullable = false, length = 100)
    // String imagen;

    // @Enumerated(EnumType.STRING)
    // private role role;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Basic
    @Column(nullable = false)
    String username;
    @Column(nullable = false)
    String lastname;

    String password;
    @Enumerated(EnumType.STRING) 
    role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority((role.name())));
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
