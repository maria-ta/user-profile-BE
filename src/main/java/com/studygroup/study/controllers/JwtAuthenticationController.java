package com.studygroup.study.controllers;

import java.util.Optional;

import com.studygroup.study.enteties.Student;
import com.studygroup.study.models.JwtRequest;
import com.studygroup.study.models.JwtResponse;
import com.studygroup.study.repos.UserRepo;
import com.studygroup.study.services.JwtUserDetailsService;
import com.studygroup.study.services.LoggingService;
import com.studygroup.study.utils.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

// according to example on medium
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LoggingService loggingService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtRequest authenticationRequest
    ) {
        try {
            String username = authenticationRequest.getUsername();
            String password = authenticationRequest.getPassword();
            Optional<Student> optionalUser = userRepo.findByUsername(username);

            if (optionalUser.isPresent()) {
                authenticate(username, password);

                final UserDetails userDetails = userDetailsService
                        .loadUserByUsername(authenticationRequest.getUsername());
                final String token = jwtTokenUtil.generateToken(userDetails);
                loggingService.login();

                return ResponseEntity.ok(new JwtResponse(token));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}