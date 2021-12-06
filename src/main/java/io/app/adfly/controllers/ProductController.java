package io.app.adfly.controllers;

import io.app.adfly.domain.dto.*;
import io.app.adfly.domain.mapper.IMapper;
import io.app.adfly.entities.Product;
import io.app.adfly.repositories.ProductRepository;
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

    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PaginatedResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationFailedResponse.class),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> GetProducts(@RequestParam int startAt, @RequestParam int count)
    {

        var request = new PaginatedRequest();
        request.setCount(count);
        request.setStartAt(startAt);
        Pageable pageable = mapper.PaginatedRequestToPageable(request);
        var user = userService.GetCurrentUser();

        if(!user.isPresent())
            return ResponseEntity.status(401).build();

        var products = productRepository.findAllByUser( pageable, user.get());
        var list = List.copyOf(products.toList());
        var mappedProducts = mapper.ListProductToListProductDto(list);
        PaginatedResponse<ProductDto> paginatedResponse = mapper.ListToPaginatedResponse(mappedProducts, request , products.getTotalPages());

        return ResponseEntity.ok(paginatedResponse);
    }

}
