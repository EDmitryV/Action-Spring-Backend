package com.actiongroup.actionserver.controllers.auth;

import com.actiongroup.actionserver.models.auth.AuthenticationRequest;
import com.actiongroup.actionserver.models.auth.AuthenticationSuccessResponse;
import com.actiongroup.actionserver.models.auth.RegisterRequest;
import com.actiongroup.actionserver.services.auth.AuthenticationService;
import com.actiongroup.actionserver.services.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authService;
  private final UserService userService;
  private final Environment env;

  @PostMapping("/register")
  public ResponseEntity<?> register(
      @RequestBody RegisterRequest request
  ) {
    var error = authService.getErrorsMessageRegister(request);
    if(error != null){
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(authService.register(request));
  }
  @PostMapping("/authentication")
  public ResponseEntity<AuthenticationSuccessResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(authService.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    authService.refreshToken(request, response);
  }

  @GetMapping("/exists/by-username/{username}")
  public ResponseEntity<Boolean> existsByUsername(@PathVariable("username")String username){
    return new ResponseEntity<>(userService.existsByUsername(username), HttpStatus.OK);
  }

  @GetMapping("/exists/by-email/{email}")
  public ResponseEntity<Boolean> existsByEmail(@PathVariable("email")String email){
    return new ResponseEntity<>(userService.existsByEmail(email), HttpStatus.OK);
  }

  @GetMapping("/get-tokens-expiration-time")
  public ResponseEntity<Map<String, String>> getTokensExpirationTime(){
    Map<String, String> response = new HashMap<>();
    response.put("auth_expiration", env.getProperty("application.security.jwt.expiration"));
    response.put("refresh_expiration", env.getProperty("application.security.jwt.refresh-token.expiration"));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
