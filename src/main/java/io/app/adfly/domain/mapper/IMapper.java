package io.app.adfly.domain.mapper;

import io.app.adfly.domain.dto.*;
import io.app.adfly.entities.*;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public interface IMapper {
    UserDto UserToUserView(User user);
    User CreateUserRequestToUser(CreateUserRequest request);
    CompanyDto CompanyToCompanyView(Company company);
    Pageable PaginatedRequestToPageable(PaginatedRequest request);
    <T> PaginatedResponse<T> ListToPaginatedResponse(List<T> list, PaginatedRequest request, int totalCount);
    ProductDto ProductToProductDto(Product product);
    ProductRewardingDto ProductRewardingToProductRewardingDto(ProductRewarding productRewarding);
    <T> ArrayList ListToListDto(List<T> entityList);
    Product ProductRequestToProduct(ProductRequest request);
    Product ProductRequestToProduct(ProductRequest request, Product source);
    ProductRewarding ProductRewardingRequestToProductRewarding(ProductRewardingRequest request);
    ProductRewarding ProductRewardingRequestToProductRewarding(ProductRewardingRequest request, ProductRewarding source);
    CategoryDto CategoryToCategoryDto(Category category);
    Category CategoryRequestToCategory(CategoryRequest request);
    Category CategoryRequestToCategory(CategoryRequest request, Category source);
}