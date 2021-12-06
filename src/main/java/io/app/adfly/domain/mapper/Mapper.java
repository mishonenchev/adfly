package io.app.adfly.domain.mapper;

import io.app.adfly.domain.dto.CreateUserRequest;
import io.app.adfly.domain.dto.UserView;
import io.app.adfly.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper implements IMapper{

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserView UserToUserView(User user) {
        var uv = new UserView();
        uv.setUsername(user.getUsername());
        uv.setId(user.getId().toString());
        uv.setFullName(user.getFullName());

        return uv;
    }

    @Override
    public User CreateUserRequestToUser(CreateUserRequest request) {
        var user = new User();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return user;
    }
}
