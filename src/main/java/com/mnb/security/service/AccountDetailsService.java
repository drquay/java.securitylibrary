package com.mnb.security.service;

import com.mnb.security.model.Account;
import com.mnb.security.model.AccountDetails;
import com.mnb.security.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account Not Found with username: " + username));
        return AccountDetails.build(account);
    }
}
