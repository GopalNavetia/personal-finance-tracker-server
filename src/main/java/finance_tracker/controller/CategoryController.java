package finance_tracker.controller;


import finance_tracker.dto.CategoryRequest;
import finance_tracker.exception.ResourceNotFoundException;
import finance_tracker.model.Category;
import finance_tracker.model.User;
import finance_tracker.repository.UserRepository;
import finance_tracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;

    private final UserRepository userRepository;



    @PostMapping
    public ResponseEntity<Category> addCategory(
            @Valid @RequestBody CategoryRequest request,
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        Category category =
                categoryService.addCategory(
                        request,
                        user.getId()
                );


        return new ResponseEntity<>(
                category,
                HttpStatus.CREATED
        );

    }




    @GetMapping
    public ResponseEntity<List<Category>> getCategories(
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        List<Category> categories =
                categoryService.getCategories(
                        user.getId()
                );


        return ResponseEntity.ok(categories);

    }





    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable String id,
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        categoryService.deleteCategory(
                id,
                user.getId()
        );


        return ResponseEntity.ok(
                "Category deleted successfully"
        );

    }




    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CategoryRequest request,
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        Category category =
                categoryService.updateCategory(
                        id,
                        request,
                        user.getId()
                );


        return ResponseEntity.ok(category);

    }

}