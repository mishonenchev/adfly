package io.app.adfly.domain.mapper;

import io.app.adfly.domain.dto.*;
import io.app.adfly.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public Pageable PaginatedRequestToPageable(PaginatedRequest request) {
        int pageNumber = request.getStartAt()/request.getCount();
        Pageable paging = PageRequest.of(pageNumber, request.getCount(), Sort.by("id"));
        return paging;
    }

    @Override
    public <T> PaginatedResponse<T> ListToPaginatedResponse(List<T> list, PaginatedRequest request, int totalCount) {
        var paginateResponse = new PaginatedResponse<T>();
        paginateResponse.setData(list);
        paginateResponse.setStartAt(request.getStartAt());
        paginateResponse.setCount(request.getCount());
        paginateResponse.setTotalCount(totalCount);
        return paginateResponse;
    }

    @Override
    public ProductDto ProductToProductDto(Product product) {
        var pdto = new ProductDto();
        pdto.setName(product.getName());
        pdto.setDescription(product.getDescription());
        pdto.setId(product.getId());
        pdto.setProductRewarding(ProductRewardingToProductRewardingDto(product.getProductRewarding()));
        if (product.getCategories() != null) {
            var categoriesDto = new HashSet<CategoryDto>();
            for (var category : product.getCategories()
            ) {
                categoriesDto.add(CategoryToCategoryDto(category));
            }
            pdto.setCategories(categoriesDto);
        }
        return pdto;
    }

    @Override
    public ProductRewardingDto ProductRewardingToProductRewardingDto(ProductRewarding productRewarding) {
        var prdto = new ProductRewardingDto();
        prdto.setRewardingStrategy(productRewarding.getRewardingStrategy());
        prdto.setAmount(productRewarding.getAmount());
        prdto.setRewardingType(productRewarding.getRewardingType());
        return prdto;
    }

    @Override
    public <T> ArrayList ListToListDto (List<T> entityList) {
        if (entityList.get(0) instanceof Product){
            var productList = new ArrayList<ProductDto>();
            for (var product :
                    (List<Product>) entityList) {
                productList.add(ProductToProductDto(product));
            }
            return productList;
        }
        if (entityList.get(0) instanceof Category){
            var categoryList = new ArrayList<CategoryDto>();
            for (var category:
                    (List<Category>) entityList){
                categoryList.add(CategoryToCategoryDto(category));
            }
            return categoryList;
        }
        return null;
    }

    @Override
    public Product ProductRequestToProduct(ProductRequest request) {
        return ProductRequestToProduct(request, new Product());
    }

    @Override
    public Product ProductRequestToProduct(ProductRequest request, Product source) {
        source.setDescription(request.getDescription());
        source.setName(request.getName());
        return source;
    }

    @Override
    public ProductRewarding ProductRewardingRequestToProductRewarding(ProductRewardingRequest request) {
        return ProductRewardingRequestToProductRewarding(request, new ProductRewarding());
    }

    @Override
    public ProductRewarding ProductRewardingRequestToProductRewarding(ProductRewardingRequest request, ProductRewarding source) {
        source.setRewardingType(request.getRewardingType());
        source.setRewardingStrategy(request.getRewardingStrategy());
        source.setAmount(request.getAmount());
        return source;
    }
    @Override
    public CategoryDto CategoryToCategoryDto(Category category) {
        var categoryDto = new CategoryDto();
        categoryDto.setDescription(category.getDescription());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
    @Override
    public Category CategoryRequestToCategory(CategoryRequest request){
        return CategoryRequestToCategory(request, new Category());
    }
    @Override
    public Category CategoryRequestToCategory(CategoryRequest request, Category source){
        source.setName(request.getName());
        source.setDescription(request.getDescription());
        return source;
    }
}