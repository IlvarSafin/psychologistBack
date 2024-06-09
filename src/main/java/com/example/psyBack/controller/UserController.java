package com.example.psyBack.controller;

import com.example.psyBack.dto.JwtAuthenticationResponse;
import com.example.psyBack.dto.SignInRequest;
import com.example.psyBack.dto.SignUpPatientRequest;
import com.example.psyBack.dto.SignUpPsychologistRequest;
import com.example.psyBack.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up-patient")
    public JwtAuthenticationResponse signUpPatient(@RequestBody SignUpPatientRequest request)
    {
        return authenticationService.signUpPatient(request);
    }

    @PostMapping("/sign-up-psychologist")
    public JwtAuthenticationResponse signUpPsychologist(@RequestBody SignUpPsychologistRequest request)
    {
        return authenticationService.signUpPsychologist(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @GetMapping("/confirm")
    public boolean confirmEmail(@RequestParam("code") String code) {
        return authenticationService.confirmEmail(code);
    }
}
