package io.app.adfly.controllers;

import io.app.adfly.domain.dto.*;
import io.app.adfly.domain.exceptions.RecordNotFoundException;
import io.app.adfly.domain.exceptions.ValidationException;
import io.app.adfly.domain.mapper.Mapper;
import io.app.adfly.entities.Category;
import io.app.adfly.entities.Product;
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/secure")
@RequiredArgsConstructor
@Api(description = "Categories operations")
public class CategoryController extends ResponseEntityExceptionHandler {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    // Company endpoints

    @GetMapping("/company/categories")
    @RolesAllowed({Role.USER_COMPANY})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PaginatedResponse.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> GetCompanyCategories(@RequestParam(defaultValue = "0", required = false) int startAt,
                                           @RequestParam(defaultValue = "10", required = false) int count){
        var request = new PaginatedRequest(startAt, count);
        Pageable pageable = Mapper.map(request, Pageable.class);
        var user = userService.GetCurrentUser();

        if(!user.isPresent())
            return ResponseEntity.status(401).build();

        var categories = categoryRepository.getAll(pageable);
        var mappedCategories = Mapper.mapList(categories.toList(), CategoryDto.class);
        PaginatedResponse<CategoryDto> paginatedResponse = Mapper.mapPaginatedResponse(mappedCategories, request , (int)categories.getTotalElements());

        return ResponseEntity.ok(paginatedResponse);
    }
    @PostMapping("/company/categories")
    @RolesAllowed({Role.USER_COMPANY})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> CreateCategory(@RequestBody CategoryRequest request){
        var user = userService.GetCurrentUser();
        if(!user.isPresent())
            return ResponseEntity.status(401).build();
        var category = Mapper.map(request, Category.class);
        categoryService.createCategory(category);
        var categoryDto = Mapper.map(category, CategoryDto.class);
        return ResponseEntity.ok(categoryDto);
    }

    @PutMapping("/company/categories/{id}")
    @RolesAllowed({Role.USER_COMPANY})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> UpdateCategory(@RequestBody CategoryRequest request, @PathVariable Long id){
        var category = categoryRepository.getById(id);
        Mapper.map(request, category);
        categoryRepository.save(category);
        var updatedCategoryDto = Mapper.map(category, CategoryDto.class);
        return ResponseEntity.ok(updatedCategoryDto);
    }
    @DeleteMapping("/company/categories/{id}")
    @RolesAllowed({Role.USER_COMPANY})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> DeleteCategory(@PathVariable Long id ){
        var category = categoryRepository.findById(id);
        if(!category.isPresent())
            throw new RecordNotFoundException("Category id not found");
        categoryRepository.delete(category.get());
        return ResponseEntity.ok().build();
    }


    // Advertiser endpoints

    @GetMapping("/advertiser/categories")
    @RolesAllowed({Role.USER_ADVERTISER})
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
    @RolesAllowed({Role.USER_ADVERTISER})
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
    @RolesAllowed({Role.USER_ADVERTISER})
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
