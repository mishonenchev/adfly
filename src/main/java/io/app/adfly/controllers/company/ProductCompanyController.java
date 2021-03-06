package io.app.adfly.controllers.company;

import io.app.adfly.domain.dto.*;
import io.app.adfly.domain.exceptions.RecordNotFoundException;
import io.app.adfly.domain.exceptions.ValidationException;
import io.app.adfly.domain.mapper.Mapper;
import io.app.adfly.entities.Product;
import io.app.adfly.entities.ProductRewarding;
import io.app.adfly.entities.Role;
import io.app.adfly.repositories.*;
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

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/secure/company/products")
@RequiredArgsConstructor
@Api(description = "Products operations")
@RolesAllowed({Role.USER_COMPANY})
public class ProductCompanyController {
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductRewardingRepository productRewardingRepository;
    private final SiteRepository siteRepository;

    @GetMapping("{productId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class)
    }
    )
    public ResponseEntity<?> GetProduct(@PathVariable Long productId)
    {
        var user = userService.GetCurrentUser();
        if(!user.isPresent())
            return ResponseEntity.status(401).build();

        var product = productRepository.findById(productId).orElseThrow(()-> new RecordNotFoundException(""));
        if(!product.getUser().equals(user.get())) throw new ValidationException("Product is not managed by this user");
        var productDto =  Mapper.map(product, ProductDto.class);

        return ResponseEntity.ok(productDto);
    }

    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PaginatedResponse.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> GetProducts(@RequestParam(defaultValue = "0", required = false) int startAt,
                                         @RequestParam(defaultValue = "10", required = false) int count)
    {

        var request = new PaginatedRequest(startAt, count);
        Pageable pageable = Mapper.map(request, Pageable.class);
        var user = userService.GetCurrentUser();

        if(!user.isPresent())
            return ResponseEntity.status(401).build();

        var products = productRepository.findAllByUser(user.get(), pageable);
        var list = List.copyOf(products.toList());
        var mappedProducts =  Mapper.mapList(list, ProductDto.class);
        PaginatedResponse<ProductDto> paginatedResponse = Mapper.mapPaginatedResponse(mappedProducts, request, (int)products.getTotalElements());

        return ResponseEntity.ok(paginatedResponse);
    }

    @PostMapping("")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> CreateProduct(@RequestBody ProductRequest request)
    {
        var user = userService.GetCurrentUser();
        if(!user.isPresent()) return ResponseEntity.status(401).build();

        var product = Mapper.map(request, Product.class);
        var rewarding = Mapper.map(request.getProductRewarding(), ProductRewarding.class);

        rewarding = productRewardingRepository.save(rewarding);
        product.setProductRewarding(rewarding);
        product.setUser(user.get());
        product.setProductStatus(Product.ProductStatus.Active);
        product = productRepository.save(product);


       ProductDto productDto = Mapper.map(product, ProductDto.class);
       return ResponseEntity.ok(productDto);

    }
    @PutMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> UpdateProduct(@RequestBody ProductRequest request, @PathVariable Long id){
        var product = productRepository.getById(id);
        Mapper.map(request, product);
        productRepository.save(product);

        var productRewarding = productRewardingRepository.getById(product.getProductRewarding().getId());
        Mapper.map(request.getProductRewarding(), productRewarding);

        productRewardingRepository.save(productRewarding);

        ProductDto productDto = Mapper.map(product, ProductDto.class);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> DeleteProduct(@PathVariable Long id ){
        var product = productRepository.findById(id);
        if(!product.isPresent()) throw new RecordNotFoundException("Product id not found");
        var entity = product.get();
        entity.setProductStatus(Product.ProductStatus.Retired);
        productRepository.save(entity);
        return ResponseEntity.ok().build();
    }


    @PostMapping("{productId}/categories/{categoryId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    }
    )
    public ResponseEntity<?> AddProductCategory(@PathVariable Long productId, @PathVariable Long categoryId){
        var user = userService.GetCurrentUser();
        if(!user.isPresent()) return ResponseEntity.status(401).build();
        var product = productRepository.findById(productId).orElseThrow(()-> new RecordNotFoundException("Product id is not found"));
        var category = categoryRepository.findById(categoryId).orElseThrow(()-> new RecordNotFoundException("Category id is not found"));;

        if(product.getCategories().contains(category)) throw new ValidationException("Category already exist for this product");

        if(!product.getUser().getId().equals(user.get().getId())) throw new ValidationException("Product is not managed by this user");

        product.getCategories().add(category);
        productRepository.save(product);
        category.getProducts().add(product);
        categoryRepository.save(category);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{productId}/categories/{categoryId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    }
    )
    public ResponseEntity<?> DeleteProductCategory(@PathVariable Long productId, @PathVariable Long categoryId)
    {
        var user = userService.GetCurrentUser();
        if(!user.isPresent()) return ResponseEntity.status(401).build();
        var product = productRepository.findById(productId).orElseThrow(()-> new RecordNotFoundException("Product id is not found"));
        var category = categoryRepository.findById(categoryId).orElseThrow(()-> new RecordNotFoundException("Category id is not found"));;

        if(!product.getCategories().contains(category)) throw new ValidationException("Category does not exist for this product");

        if(!product.getUser().getId().equals(user.get().getId())) throw new ValidationException("Product is not managed by this user");

        product.getCategories().remove(category);
        productRepository.save(product);
        category.getProducts().remove(product);
        categoryRepository.save(category);

        return ResponseEntity.ok().build();
    }

    @PostMapping("{productId}/site/{siteId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    }
    )
    public ResponseEntity<?> AddProductSite(@PathVariable Long productId, @PathVariable Long siteId){
        var user = userService.GetCurrentUser();
        if(!user.isPresent()) return ResponseEntity.status(401).build();
        var product = productRepository.findById(productId).orElseThrow(()-> new RecordNotFoundException("Product id is not found"));
        if(!product.getUser().getId().equals(user.get().getId())) throw new ValidationException("Product is not managed by this user");

        var site = siteRepository.findById(siteId).orElseThrow(()-> new RecordNotFoundException("Site id is not found"));
        if(product.getSite()!=null) throw new ValidationException("Product already has a site attached. Consider removing it first.");

        product.setSite(site);
        productRepository.save(product);
        var productDto = Mapper.map(product, ProductDto.class);

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("{productId}/site")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    }
    )
    public ResponseEntity<?> RemoveProductSite(@PathVariable Long productId){
        var user = userService.GetCurrentUser();
        if(!user.isPresent()) return ResponseEntity.status(401).build();
        var product = productRepository.findById(productId).orElseThrow(()-> new RecordNotFoundException("Product id is not found"));
        if(!product.getUser().getId().equals(user.get().getId())) throw new ValidationException("Product is not managed by this user");
        if(product.getSite()==null) throw new ValidationException("Product has no a site attached.");

        product.setSite(null);
        productRepository.save(product);
        var productDto = Mapper.map(product, ProductDto.class);

        return ResponseEntity.ok(productDto);
    }

}
