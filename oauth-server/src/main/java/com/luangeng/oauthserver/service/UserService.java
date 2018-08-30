package com.luangeng.oauthserver.service;

import com.luangeng.oauthserver.dao.UserRepository;
import com.luangeng.oauthserver.vo.Role;
import com.luangeng.oauthserver.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByName(s);
        if (user == null) {
            throw new UsernameNotFoundException("user not found " + s);
        }
        return new CustomUserDetails(user);
    }


    class CustomUserDetails implements UserDetails {

        private static final long serialVersionUID = 1L;
        private Collection<? extends GrantedAuthority> authorities;
        private User user;

        public CustomUserDetails(User user) {
            this.user = user;
            this.authorities = translate(user.getRole());
        }

        private Collection<? extends GrantedAuthority> translate(List<Role> roles) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Role role : roles) {
                String name = role.getName().toUpperCase();
                if (!name.startsWith("ROLE_")) {
                    name = "ROLE_" + name;
                }
                authorities.add(new SimpleGrantedAuthority(name));
            }
            return authorities;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getName();
        }


        @Override
        public boolean isAccountNonExpired() {
            return !user.getExpire();
        }

        @Override
        public boolean isAccountNonLocked() {
            return !user.getLock();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return !user.getDisable();
        }

    }

}
