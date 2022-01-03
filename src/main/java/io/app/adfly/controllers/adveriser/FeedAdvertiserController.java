package io.app.adfly.controllers.adveriser;

import io.app.adfly.domain.dto.PaginatedRequest;
import io.app.adfly.domain.dto.PaginatedResponse;
import io.app.adfly.domain.dto.ProductDto;
import io.app.adfly.domain.mapper.Mapper;
import io.app.adfly.entities.Role;
import io.app.adfly.repositories.ProductRepository;
import io.app.adfly.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/secure/advertiser/feed")
@RequiredArgsConstructor
@RolesAllowed({Role.USER_ADVERTISER})
public class FeedAdvertiserController {

    private final UserService userService;
    private final ProductRepository productRepository;

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PaginatedResponse.class)
    }
    )
    public ResponseEntity<?> GetFeed(@RequestParam(defaultValue = "0", required = false) int startAt,
                                     @RequestParam(defaultValue = "10", required = false) int count){

        var user = userService.GetCurrentUser();
        if (user.isEmpty())
            return ResponseEntity.status(401).build();
        var userCategories = user.get().advertiser.getCategories();
        var request = new PaginatedRequest(startAt, count);
        Pageable pageable = Mapper.map(request, Pageable.class);
        var userFeed = productRepository.getAllByUserCategories(userCategories, pageable);
        var mappedFeed = Mapper.mapList(userFeed.toList(), ProductDto.class);
        PaginatedResponse<ProductDto> paginatedResponse = Mapper.mapPaginatedResponse(mappedFeed, request, (int) userFeed.getTotalElements());
        return ResponseEntity.ok(paginatedResponse);
    }
}
