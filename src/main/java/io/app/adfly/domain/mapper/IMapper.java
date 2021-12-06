package io.app.adfly.domain.mapper;

import io.app.adfly.domain.dto.CreateUserRequest;
import io.app.adfly.domain.dto.UserView;
import io.app.adfly.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapper {
    UserView UserToUserView(User user);
    User CreateUserRequestToUser(CreateUserRequest request);
}
