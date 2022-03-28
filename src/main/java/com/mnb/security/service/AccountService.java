package com.mnb.security.service;

import com.mnb.security.common.JwtUtils;
import com.mnb.security.model.Account;
import com.mnb.security.model.AccountDetails;
import com.mnb.security.model.Role;
import com.mnb.security.repository.AccountRepository;
import com.mnb.security.repository.RoleRepository;
import com.mnb.security.rest.payload.request.LoginRequest;
import com.mnb.security.rest.payload.request.SignupRequest;
import com.mnb.security.rest.payload.response.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder encoder;

    public JwtResponse signIn(LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtUtils.generateJwtToken(authentication);

        final AccountDetails userDetails = (AccountDetails) authentication.getPrincipal();
        final List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
    }

    public boolean signUp(SignupRequest signUpRequest) {
        if (accountRepository.existsByUsername(signUpRequest.getUsername())) {
            return false;
        }
        final Account user = new Account(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));
        final Set<String> strRoles = signUpRequest.getRoles();
        final Set<Role> roles = strRoles.stream().map(role -> roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."))).collect(Collectors.toSet());
        user.setRoles(roles);
        accountRepository.save(user);
        return true;
    }
}
