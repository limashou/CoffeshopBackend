package spring.backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import spring.backend.dto.*;
import spring.backend.exception.AppRuntimeException;
import spring.backend.exception.AppUsernameNotFoundException;
import spring.backend.service.JwtService;
import spring.backend.service.RefreshTokenService;
import spring.backend.service.UserService;
import spring.backend.entity.RefreshToken;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    RefreshTokenService refreshTokenService;
    UserService userService;

    @PostMapping(value = "/save")
    public ResponseEntity<String> saveUser(@RequestBody UserRequest userRequest) {
        if(userRequest != null) {
            userService.saveUser(userRequest);
            return ResponseEntity.ok("User saved successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Body is empty");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PostMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserResponse userResponse = userService.getUser();
            return ResponseEntity.ok().body(userResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/getAllInformation")
    public ResponseEntity<UserRequest> getSendUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserRequest userRequest = userService.getAllInformationUser();
            return ResponseEntity.ok().body(userRequest);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody UserResponse userResponse) {
        if (userResponse.getId() != null) {
            userService.deleteUserById(userResponse.getId());
            return ResponseEntity.ok("User successfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/login")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(authentication.isAuthenticated()){
            return JwtResponseDTO.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                    .build();
        } else {
            throw new AppUsernameNotFoundException("invalid user request..!!", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.GenerateToken(user.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .accessToken(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() ->new AppRuntimeException("Refresh Token is not in DB..!!",HttpStatus.NOT_FOUND));
    }
}
