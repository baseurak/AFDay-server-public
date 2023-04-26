package com.baseurak.AFDay.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * @Author: Ru
 */
@Data
@AllArgsConstructor
@Entity
@Table(name="users")
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(){}

    @Builder
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> gList = new ArrayList<>();
        GrantedAuthority g = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                //return "ROLE_" + role.toString();
                return role.toString();
            }
        };
        gList.add(g);

        return gList;
    }


    @Override
    public String getUsername() {
        return email;
    }

    // 계정 만료 확인, true : 만료안됨
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 확인, trueh : 잠금x
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    //public Object update(String name) {}
}