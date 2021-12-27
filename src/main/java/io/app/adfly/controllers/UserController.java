package io.app.adfly.controllers;
import io.app.adfly.domain.dto.*;
import io.app.adfly.domain.exceptions.ValidationException;
import io.app.adfly.domain.mapper.Mapper;
import io.app.adfly.entities.Role;
import io.app.adfly.entities.User;
import io.app.adfly.repositories.UserRepository;
import io.app.adfly.services.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/secure/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @GetMapping()
    @RolesAllowed({Role.USER_ADVERTISER, Role.USER_COMPANY})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = UserDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found")
    }
    )
    public ResponseEntity<?> GetProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        var user = userRepository.findByUsername(currentPrincipalName);

        if(user.isPresent()) {
            var userDto = Mapper.map(user.get(), UserDto.class);
            return ResponseEntity.ok(userDto);
        }

        return ResponseEntity.status(401).build();
    }

    @PostMapping("change-password")
    @RolesAllowed({Role.USER_ADVERTISER, Role.USER_COMPANY})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found")
    }
    )
    public ResponseEntity<?> ChangePassword(@RequestBody @Valid ChangePasswordRequest request)
    {
        try {
            if(!Objects.equals(request.getPassword(), request.getRePassword()))
                throw new ValidationException("Passwords do not match");
            var user = userService.GetCurrentUser();
            if(user.isPresent()){
                var userObj =  user.get();

               var auth =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userObj.getUsername(), request.getOldPassword()));

                userObj.setPassword(passwordEncoder.encode(request.getPassword()));
                userRepository.save(userObj);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(401).build();
        }catch (Exception e){
            throw new ValidationException("Old password is wrong");

        }

    }
}
