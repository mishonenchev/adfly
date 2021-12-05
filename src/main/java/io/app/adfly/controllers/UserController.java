package io.app.adfly.controllers;
import io.app.adfly.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping()
    public ResponseEntity<?> GetUsers(){
        return ResponseEntity.ok("12");
    }
}
