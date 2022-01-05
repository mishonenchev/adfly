package io.app.adfly.controllers;

import io.app.adfly.config.security.JwtTokenUtil;
import io.app.adfly.domain.dto.CreateUserRequest;
import io.app.adfly.domain.dto.UserDto;
import io.app.adfly.domain.mapper.Mapper;
import io.app.adfly.domain.dto.AuthRequest;
import io.app.adfly.entities.User;
import io.app.adfly.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;

@Tag(name = "Authentication")
@RestController
@RequestMapping(path = "api/public")
@RequiredArgsConstructor
public class AuthController extends ResponseEntityExceptionHandler {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @PostMapping("login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = UserDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            User user = (User) authenticate.getPrincipal();
            var userView = Mapper.map(user, UserDto.class);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateAccessToken(user))
                    .body(userView);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("register")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = UserDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserRequest request) {
            return ResponseEntity.ok(userService.create(request));
    }
}
