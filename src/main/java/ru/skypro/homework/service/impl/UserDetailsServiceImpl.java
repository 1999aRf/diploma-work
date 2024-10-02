package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserPrincipal;
import ru.skypro.homework.repositories.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsManager {

    private UserRepository userRepository;
    private UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> new User(user.getEmail(),
                        user.getPassword(),
                        getAuthorities(user.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("%s - not found", username)));
    }

    public List<GrantedAuthority> getAuthorities(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority("Role_" + role.name()));
    }

    @Override
    public void createUser(UserDetails user) {
        userRepository.save(mapper.userDetailsToUser(user));
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByEmail(username).isPresent();
    }
}
