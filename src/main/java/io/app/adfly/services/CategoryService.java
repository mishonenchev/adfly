package io.app.adfly.services;

import io.app.adfly.domain.exceptions.RecordNotFoundException;
import io.app.adfly.domain.exceptions.ValidationException;
import io.app.adfly.entities.Category;
import io.app.adfly.entities.User;
import io.app.adfly.repositories.CategoryRepository;
import io.app.adfly.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public void createCategory(Category category){
        if(!categoryRepository.existsByName(category.getName())){
            categoryRepository.save(category);
        }
        else throw new ValidationException("Category with this name already exists");
    }
    public void addCategory(User user, Optional<Category> category){
        if (category.isEmpty())
            throw new RecordNotFoundException("Category not found");

        var userCategories = user.advertiser.getCategories();

        if (userCategories.contains(category.get()))
            throw new ValidationException("Category already added");

        userCategories.add(category.get());
        user.advertiser.setCategories(userCategories);
        userRepository.save(user);
    }

    public void removeCategory(User user, Optional<Category> category) {
        if(category.isEmpty())
            throw new RecordNotFoundException("Category not found");
        if(!user.advertiser.getCategories().contains(category.get()))
            throw new RecordNotFoundException("Category isn't added");
        var userCategories = user.advertiser.getCategories();
        userCategories.remove(category.get());
        user.advertiser.setCategories(userCategories);
        userRepository.save(user);
    }
}
