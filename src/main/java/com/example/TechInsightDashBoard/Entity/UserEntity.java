package com.example.TechInsightDashBoard.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(name = "username", nullable = false, length = 100)
 private String username;

 @Column(name = "email", nullable = false, unique = true, length = 255)
 private String email;

 @Column(name = "password", nullable = false)
 private String password;

 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
 private List<TechEntity> techEntities = new ArrayList<>();


 @Override
 public Collection<? extends GrantedAuthority> getAuthorities() {
  return List.of();
 }

 @Override
 public String getUsername() {
     return email;
 }

 public String getDisplayName() {
     return username;
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
