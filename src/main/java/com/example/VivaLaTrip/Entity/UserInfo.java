package com.example.VivaLaTrip.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@Entity
public class UserInfo implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserId;

    @Column(name="ID",length = 500,nullable = false)
    private String ID;

    @Column(name="PW",length = 300,nullable = false)
    private String PW;

    @Column(name="NickName",length = 200,nullable = false)
    private String NickName;
/*

    @Column(name="liked",length = 800)
    private String liked;
*/

    @Column(name="Authority")
    private String Authority;

    @Column(name = "Check_Email",length = 200)
    private String Check_Email;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : Authority.split("_")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    @Override
    public String getPassword() {
        return PW;
    }

    @Override
    public String getUsername() {
        return ID;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } //유저 아이디가 만료 되었는지

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }  //유저 아이디가 lock걸렸는지

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }  //비밀번호가 만료 되었는지

    @Override
    public boolean isEnabled() {
        return true;
    }  //계정이 활성화 되었는지
}