package io.app.adfly.domain.mapper;

import io.app.adfly.domain.dto.*;
import io.app.adfly.entities.Product;
import io.app.adfly.entities.ProductRewarding;
import io.app.adfly.entities.User;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;


public interface IMapper {
    UserView UserToUserView(User user);
    User CreateUserRequestToUser(CreateUserRequest request);

    Pageable PaginatedRequestToPageable(PaginatedRequest request);

    <T> PaginatedResponse<T> ListToPaginatedResponse(List<T> list, PaginatedRequest request, int totalCount);

    ProductDto ProductToProductDto(Product product);
    ProductRewardingDto ProductRewardingToProductRewardingDto(ProductRewarding productRewarding);

    List<ProductDto> ListProductToListProductDto(List<Product> products);

    Product ProductRequestToProduct(ProductRequest request);
    Product ProductRequestToProduct(ProductRequest request, Product source);

    ProductRewarding ProductRewardingRequestToProductRewarding(ProductRewardingRequest request);

    ProductRewarding ProductRewardingRequestToProductRewarding(ProductRewardingRequest request, ProductRewarding source);
}
