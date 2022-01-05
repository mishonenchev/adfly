package io.app.adfly.controllers.adveriser;

import io.app.adfly.domain.dto.CategoryDto;
import io.app.adfly.domain.dto.PaginatedRequest;
import io.app.adfly.domain.dto.PaginatedResponse;
import io.app.adfly.domain.dto.ProductDto;
import io.app.adfly.domain.mapper.Mapper;
import io.app.adfly.entities.Category;
import io.app.adfly.entities.Role;
import io.app.adfly.repositories.CategoryRepository;
import io.app.adfly.services.CategoryService;
import io.app.adfly.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/secure")
@RequiredArgsConstructor
@Api(description = "Categories operations")
@RolesAllowed({Role.USER_ADVERTISER})
public class CategoryAdvertiserController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @GetMapping("/advertiser/categories")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PaginatedResponse.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> GetAdvertiserCategories(@RequestParam(defaultValue = "0", required = false) int startAt,
                                                     @RequestParam(defaultValue = "10", required = false) int count,
                                                     @RequestParam(defaultValue = "false") Boolean filterOwned)
    {
        var request = new PaginatedRequest(startAt, count);
        Pageable pageable = Mapper.map(request, Pageable.class);
        var user = userService.GetCurrentUser();
        if (user.isEmpty())
            return ResponseEntity.status(401).build();
        Page<Category> categories;
        if (filterOwned)
            categories = categoryRepository.findAllByAdvertiser(user.get().getId(), pageable);
        else
            categories = categoryRepository.getAll(pageable);
        var mappedCategories = Mapper.mapList(categories.toList(), CategoryDto.class);
        PaginatedResponse<CategoryDto> paginatedResponse = Mapper.mapPaginatedResponse(mappedCategories, request , (int)categories.getTotalElements());

        return ResponseEntity.ok(paginatedResponse);
    }

    @PutMapping("/advertiser/categories/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> AddCategory(@PathVariable Long id){
        var user = userService.GetCurrentUser();
        if (!user.isPresent())
            return ResponseEntity.status(401).build();
        var category = categoryRepository.findById(id);
        categoryService.addCategory(user.get(), category);
        var categoryDto = Mapper.map(category.get(), CategoryDto.class);
        return ResponseEntity.ok(categoryDto);
    }

    @DeleteMapping("/advertiser/categories/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> RemoveCategory(@PathVariable Long id){
        var user = userService.GetCurrentUser();
        if (!user.isPresent())
            return ResponseEntity.status(401).build();
        var category = categoryRepository.findById(id);
        categoryService.removeCategory(user.get(), category);
        return ResponseEntity.ok().build();
    }
}
