package io.app.adfly.controllers.adveriser;

import io.app.adfly.domain.dto.*;
import io.app.adfly.domain.exceptions.RecordNotFoundException;
import io.app.adfly.domain.exceptions.ValidationException;
import io.app.adfly.domain.mapper.Mapper;
import io.app.adfly.domain.utility.StringGenerator;
import io.app.adfly.entities.Product;
import io.app.adfly.entities.ProductAdvertiser;
import io.app.adfly.entities.ProductRewarding;
import io.app.adfly.entities.Role;
import io.app.adfly.repositories.ProductAdvertiserRepository;
import io.app.adfly.repositories.ProductRepository;
import io.app.adfly.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/secure/advertiser/products")
@RequiredArgsConstructor
@RolesAllowed({Role.USER_ADVERTISER})
public class ProductAdvertiserController {
    private final ProductAdvertiserRepository productAdvertiserRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    @PostMapping("{productId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> AddProduct(@PathVariable Long productId)
    {
        var user = userService.GetCurrentUser();
        if(!user.isPresent()) return ResponseEntity.status(401).build();

        var product = productRepository.findById(productId).orElseThrow(()-> new RecordNotFoundException("Product Id is not found"));
        if(productAdvertiserRepository.existsByProductAndAdvertiser(product, user.get().getAdvertiser()))
            throw new ValidationException("Product Id already exists for this user");

        ProductAdvertiser productAdvertiser = new ProductAdvertiser();
        productAdvertiser.setAdvertiser(user.get().getAdvertiser());
        productAdvertiser.setProduct(product);
        productAdvertiser.setExternalCode(StringGenerator.Generate(25));
        productAdvertiser.setRedirectCode(StringGenerator.Generate(7));
        productAdvertiserRepository.save(productAdvertiser);
        var productAdvertiserDto = Mapper.map(productAdvertiser, ProductAdvertiserDto.class);

        return ResponseEntity.ok(productAdvertiserDto);
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

        var products = productAdvertiserRepository.getAllByAdvertiser(user.get().getAdvertiser(), pageable);
        var list = List.copyOf(products.toList());
        var mappedProducts =  Mapper.mapList(list, ProductAdvertiserDto.class);
        PaginatedResponse<ProductAdvertiserDto> paginatedResponse = Mapper.mapPaginatedResponse(mappedProducts, request, (int)products.getTotalElements());

        return ResponseEntity.ok(paginatedResponse);
    }
}
