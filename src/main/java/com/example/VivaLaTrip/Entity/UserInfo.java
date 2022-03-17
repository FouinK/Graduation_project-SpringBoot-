package com.example.VivaLaTrip.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserInfo implements UserDetails {

    @Id
    @GeneratedValue
    private Long NUM;
    @Column(name="ID")
    private String ID;
    @Column(name="PW")
    private String PW;
    @Column(name="UserName")
    private String UserName;
    @Column(name="liked")
    private String liked;
    @Column(name="Authority")
    private String Authority;

    public String getID() {
        return ID;
    }

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

    public Long getNUM() {
        return NUM;
    }

    public String getUserName() {
        return UserName;
    }

    public String getLiked() {
        return liked;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public void setAuthority(String authority) {
        Authority = authority;
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
