package finance_tracker.service;


import finance_tracker.dto.CategoryRequest;
import finance_tracker.exception.ResourceNotFoundException;
import finance_tracker.exception.UnauthorizedException;
import finance_tracker.model.Category;
import finance_tracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {


    private final CategoryRepository categoryRepository;



    public Category addCategory(
            CategoryRequest request,
            String userId
    ) {


        Category category = Category.builder()
                .userId(userId)
                .name(request.getName())
                .budget(request.getBudget())
                .build();


        return categoryRepository.save(category);

    }




    public List<Category> getCategories(
            String userId
    ) {


        return categoryRepository.findByUserId(userId);

    }





    public void deleteCategory(
            String id,
            String userId
    ) {


        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found"
                                )
                        );



        if (!category.getUserId().equals(userId)) {

            throw new UnauthorizedException(
                    "You cannot delete this category"
            );

        }



        categoryRepository.delete(category);

    }



    public Category updateCategory(
            String id,
            CategoryRequest request,
            String userId
    ) {


        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found"
                                )
                        );


        if (!category.getUserId().equals(userId)) {

            throw new UnauthorizedException(
                    "You cannot update this category"
            );

        }


        category.setName(request.getName());

        category.setBudget(request.getBudget());


        return categoryRepository.save(category);

    }

}