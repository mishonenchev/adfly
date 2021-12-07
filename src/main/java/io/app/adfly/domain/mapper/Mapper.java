package io.app.adfly.domain.mapper;

import io.app.adfly.domain.dto.*;
import io.app.adfly.entities.Product;
import io.app.adfly.entities.ProductRewarding;
import io.app.adfly.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public List<ProductDto> ListProductToListProductDto(List<Product> products) {
        var productList = new ArrayList<ProductDto>();
        for (int i=0; i<products.size(); i++){
            productList.add(ProductToProductDto(products.get(i)));
        };
        return productList;
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
}
