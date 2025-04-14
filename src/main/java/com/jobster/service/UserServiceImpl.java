package com.jobster.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobster.config.JwtAuthToken;
import com.jobster.dto.request.LoginRequest;
import com.jobster.dto.request.RegisterRequest;
import com.jobster.dto.response.LoginResponse;
import com.jobster.entity.User;
import com.jobster.exception.JobExceptions;
import com.jobster.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthToken jwtAuthToken;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void register(RegisterRequest registerRequest){
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new JobExceptions("email already exists");
        }

        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new JobExceptions("username already exists");
        }
        
        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())){
            throw new JobExceptions("password and confirmpassword must match!");
        }

        if(registerRequest.getRole() == null){
            throw new JobExceptions("role must be either APPLICANT or EMPLOYER");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());

        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtAuthToken.generateToken(userDetails);
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new JobExceptions("user not found"));
        return new LoginResponse(token,user.getEmail(),user.getRole());
    }

}
