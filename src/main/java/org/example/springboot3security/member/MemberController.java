package org.example.springboot3security.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springboot3security.config.JwtTokenManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String accessToken = jwtTokenManager.createAccessToken(new Member(authentication.getName()));

        return ResponseEntity.ok(accessToken);
    }

    @GetMapping
    public ResponseEntity<String> helloWorld() {
        log.info("authentication: {}", SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("Hello World!");
    }
}
