package com.actiongroup.actionserver.controllers.auth;

import com.actiongroup.actionserver.models.auth.AuthenticationRequest;
import com.actiongroup.actionserver.models.auth.AuthenticationSuccessResponse;
import com.actiongroup.actionserver.models.auth.RegisterRequest;
import com.actiongroup.actionserver.services.auth.AuthenticationService;
import com.actiongroup.actionserver.services.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authService;
  private final UserService userService;

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
  @PostMapping("/authenticate")
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


}
