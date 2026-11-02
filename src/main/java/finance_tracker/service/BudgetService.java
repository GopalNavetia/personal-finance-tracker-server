package finance_tracker.service;


import finance_tracker.dto.BudgetResponse;
import finance_tracker.model.Category;
import finance_tracker.model.Transaction;
import finance_tracker.model.TransactionType;
import finance_tracker.repository.CategoryRepository;
import finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class BudgetService {


    private final CategoryRepository categoryRepository;

    private final TransactionRepository transactionRepository;



    public List<BudgetResponse> getBudgetSummary(
            String userId
    ) {


        List<Category> categories =
                categoryRepository.findByUserId(userId);



        List<BudgetResponse> responses =
                new ArrayList<>();



        for(Category category : categories) {


            List<Transaction> transactions =
                    transactionRepository
                            .findByUserIdAndCategoryId(
                                    userId,
                                    category.getId()
                            );


            double spent = transactions.stream()
                    .filter(transaction ->
                            transaction.getType()
                                    == TransactionType.EXPENSE
                    )
                    .mapToDouble(Transaction::getAmount)
                    .sum();



            double remaining =
                    category.getBudget() - spent;



            BudgetResponse response =
                    BudgetResponse.builder()
                            .categoryId(category.getId())
                            .categoryName(category.getName())
                            .budget(category.getBudget())
                            .spent(spent)
                            .remaining(remaining)
                            .build();



            responses.add(response);

        }


        return responses;

    }

}