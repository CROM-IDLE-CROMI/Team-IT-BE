package ssu.cromi.teamit.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ssu.cromi.teamit.domain.User;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserDetailsImpl implements UserDetails {
    private final String uid;
    private final User user;
    @JsonIgnore
    private final String password;
    private final String nickName;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(User user, String uid, String password, String nickName, Collection<? extends GrantedAuthority> authorities){
        this.user = user;
        this.uid = uid;
        this.password = password;
        this.nickName = nickName;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user){
        var authority = new SimpleGrantedAuthority(user.getRoles());
        return new UserDetailsImpl(
                user,
                user.getUid(),
                user.getPassword(),
                user.getNickName(),
                Collections.singletonList(authority)
        );
    }
    @Override
    public String getUsername(){
        return uid;
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