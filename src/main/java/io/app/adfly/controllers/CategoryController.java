package io.app.adfly.controllers;

import io.app.adfly.domain.dto.*;
import io.app.adfly.domain.exceptions.RecordNotFoundException;
import io.app.adfly.domain.mapper.Mapper;
import io.app.adfly.entities.Category;
import io.app.adfly.entities.Product;
import io.app.adfly.repositories.CategoryRepository;
import io.app.adfly.services.CategoryService;
import io.app.adfly.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/secure/company/categories")
@RequiredArgsConstructor
@Api(description = "Categories operations")
public class CategoryController extends ResponseEntityExceptionHandler {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PaginatedResponse.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden")
    }
    )
    public ResponseEntity<?> GetCategories(@RequestParam(defaultValue = "0", required = false) int startAt,
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
    @PostMapping("")
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
        categoryService.addCategory(category);
        var categoryDto = Mapper.map(category, CategoryDto.class);
        return ResponseEntity.ok(categoryDto);
    }

    @PutMapping("{id}")
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
    @DeleteMapping("{id}")
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
}
