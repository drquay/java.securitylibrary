package com.mnb.security.rest;

import com.mnb.security.rest.payload.request.LoginRequest;
import com.mnb.security.rest.payload.request.SignupRequest;
import com.mnb.security.rest.payload.response.MessageResponse;
import com.mnb.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(accountService.signIn(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return accountService.signUp(signUpRequest) ?
                ResponseEntity.ok(new MessageResponse("User registered successfully!")) :
                ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }
}
