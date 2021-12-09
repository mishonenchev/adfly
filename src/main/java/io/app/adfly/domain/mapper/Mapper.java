package io.app.adfly.domain.mapper;

import io.app.adfly.domain.dto.CompanyDto;
import io.app.adfly.domain.dto.CreateUserRequest;
import io.app.adfly.domain.dto.UserDto;
import io.app.adfly.entities.Company;
import io.app.adfly.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper implements IMapper{

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto UserToUserView(User user) {
        var uv = new UserDto();
        uv.setUsername(user.getUsername());
        uv.setId(user.getId().toString());
        uv.setFullName(user.getFullName());

        return uv;
    }

    @Override
    public CompanyDto CompanyToCompanyView(Company company){
        var cv = new CompanyDto();
        cv.setId(company.getId().toString());
        cv.setName(company.getName());
        cv.setDescription(company.getDescription());
        cv.setWebsite(company.getWebsite());
        cv.setRegisteredAddress(company.getRegisteredAddress());
        cv.setRegistrationNumber(company.getRegistrationNumber());

        return cv;
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
