package io.app.adfly.services;

import io.app.adfly.domain.exceptions.ValidationException;
import io.app.adfly.entities.Category;
import io.app.adfly.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void addCategory(Category category){
        if(!categoryRepository.existsByName(category.getName())){
            categoryRepository.save(category);
        }
        throw new ValidationException("Category with this name already exists");
    }
}
