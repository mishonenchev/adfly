package io.app.adfly.controllers;
import io.app.adfly.domain.dto.ChangePasswordRequest;
import io.app.adfly.domain.dto.UserView;
import io.app.adfly.domain.dto.ValidationError;
import io.app.adfly.domain.dto.ValidationFailedResponse;
import io.app.adfly.domain.mapper.IMapper;
import io.app.adfly.entities.Role;
import io.app.adfly.repositories.UserRepository;
import io.app.adfly.services.UserService;
import lombok.RequiredArgsConstructor;
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
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final IMapper mapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @GetMapping()
    @RolesAllowed({Role.USER_ADVERTISER, Role.USER_COMPANY})
    public ResponseEntity<?> GetProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        var user = userRepository.findByUsername(currentPrincipalName);
        if(user.isPresent()) {
            return ResponseEntity.ok(mapper.UserToUserView(user.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("change-password")
    @RolesAllowed({Role.USER_ADVERTISER, Role.USER_COMPANY})
    public ResponseEntity<?> ChangePassword(@RequestBody @Valid ChangePasswordRequest request)
    {
        try {
            if(!Objects.equals(request.getPassword(), request.getRePassword()))
                return ResponseEntity.badRequest().body(new ValidationFailedResponse(new ValidationError("Passwords do not match")));

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
            return ResponseEntity.badRequest().body(new ValidationFailedResponse(new ValidationError("Old password is wrong")));

        }

    }
}
