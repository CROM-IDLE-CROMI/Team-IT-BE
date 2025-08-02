package ssu.cromi.teamit.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository repo){
        this.userRepository = repo;
    }
    @Override
    public UserDetails loadUserByUsername(String uid)
        throws UsernameNotFoundException{
        User user = userRepository.findByUid(uid)
        .orElseThrow(() ->
                new UsernameNotFoundException("uid를 찾을 수 없음" + uid)
        );
        String roleName = user.getRoles().replaceFirst("^ROLE_", "");

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUid())
                .password(user.getPassword())
                .roles(roleName)
                .build();
    }
}
