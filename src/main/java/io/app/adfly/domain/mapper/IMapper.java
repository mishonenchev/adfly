package io.app.adfly.domain.mapper;

import io.app.adfly.domain.dto.CompanyDto;
import io.app.adfly.domain.dto.CreateUserRequest;
import io.app.adfly.domain.dto.UserDto;
import io.app.adfly.entities.Company;
import io.app.adfly.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapper {
    UserDto UserToUserView(User user);
    User CreateUserRequestToUser(CreateUserRequest request);
    CompanyDto CompanyToCompanyView(Company company);
}
