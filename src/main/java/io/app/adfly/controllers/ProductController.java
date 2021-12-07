package io.app.adfly.controllers;

import io.app.adfly.domain.dto.*;
import io.app.adfly.domain.mapper.IMapper;
import io.app.adfly.entities.Product;
import io.app.adfly.repositories.ProductRepository;
import io.app.adfly.repositories.ProductRewardingRepository;
import io.app.adfly.repositories.UserRepository;
import io.app.adfly.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/secure/company/products")
@RequiredArgsConstructor
@Api(description = "Products operations")
public class ProductController {
    private final UserService userService;
    private final IMapper mapper;
    private final ProductRepository productRepository;
    private final ProductRewardingRepository productRewardingRepository;

    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PaginatedResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationFailedResponse.class),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> GetProducts(@RequestParam(defaultValue = "0", required = false) int startAt,
                                         @RequestParam(defaultValue = "10", required = false) int count)
    {

        var request = new PaginatedRequest(startAt, count);
        Pageable pageable = mapper.PaginatedRequestToPageable(request);
        var user = userService.GetCurrentUser();

        if(!user.isPresent())
            return ResponseEntity.status(401).build();

        var products = productRepository.findAllByUser(pageable, user.get());
        var list = List.copyOf(products.toList());
        var mappedProducts = mapper.ListProductToListProductDto(list);
        PaginatedResponse<ProductDto> paginatedResponse = mapper.ListToPaginatedResponse(mappedProducts, request, (int)products.getTotalElements());

        return ResponseEntity.ok(paginatedResponse);
    }

    @PostMapping("")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationFailedResponse.class),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> CreateProduct(@RequestBody ProductRequest request)
    {
        var user = userService.GetCurrentUser();
        if(!user.isPresent()) return ResponseEntity.status(401).build();

        var product = mapper.ProductRequestToProduct(request);
        var rewarding = mapper.ProductRewardingRequestToProductRewarding(request.getProductRewarding());

        rewarding = productRewardingRepository.save(rewarding);
        product.setProductRewarding(rewarding);
        product.setUser(user.get());

       product = productRepository.save(product);

       ProductDto productDto = mapper.ProductToProductDto(product);
       return ResponseEntity.ok(productDto);

    }
    @PutMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationFailedResponse.class),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> UpdateProduct(@RequestBody ProductRequest request, @PathVariable Long id){
        var product = productRepository.getById(id);
        var updatedProduct = mapper.ProductRequestToProduct(request, product);
        productRepository.save(updatedProduct);

        var productRewarding = productRewardingRepository.getById(product.getProductRewarding().getId());
        productRewarding = mapper.ProductRewardingRequestToProductRewarding(request.getProductRewarding(), productRewarding);

        productRewardingRepository.save(productRewarding);
        updatedProduct.setProductRewarding(productRewarding);

        ProductDto productDto = mapper.ProductToProductDto(updatedProduct);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationFailedResponse.class),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> DeleteProduct(@PathVariable Long id ){
        var product = productRepository.findById(id);
        if(!product.isPresent()) return ResponseEntity.badRequest().body(new ValidationFailedResponse(new ValidationError("Product id not found")));
        var entity = product.get();
        entity.setProductStatus(Product.ProductStatus.Retired);
        productRepository.save(entity);
        return ResponseEntity.ok().build();
    }
}