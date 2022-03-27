package com.mnb.security.configuration;

import com.mnb.security.service.AccountService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AccountService.class)
public class MnbSecurityConfig {
}
