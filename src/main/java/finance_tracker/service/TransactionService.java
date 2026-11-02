package finance_tracker.service;


import finance_tracker.dto.TransactionRequest;
import finance_tracker.exception.ResourceNotFoundException;
import finance_tracker.exception.UnauthorizedException;
import finance_tracker.model.Category;
import finance_tracker.model.Transaction;
import finance_tracker.model.TransactionType;
import finance_tracker.repository.CategoryRepository;
import finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TransactionService {


    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;



    public Transaction addTransaction(
            TransactionRequest request,
            String userId
    ) {


        if(request.getType() == TransactionType.EXPENSE) {


            Category category =
                    categoryRepository.findById(request.getCategoryId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Category not found"
                                    )
                            );


            if(!category.getUserId().equals(userId)) {

                throw new UnauthorizedException(
                        "You cannot use this category"
                );
            }

        }


        Transaction transaction = Transaction.builder()
                .userId(userId)
                .type(request.getType())
                .amount(request.getAmount())
                .categoryId(request.getCategoryId())
                .description(request.getDescription())
                .date(request.getDate())
                .build();


        return transactionRepository.save(transaction);

    }



    public List<Transaction> getTransactions(
            String userId
    ) {


        return transactionRepository
                .findByUserIdOrderByDateDesc(userId);
    }




    public void deleteTransaction(
            String id,
            String userId
    ) {


        Transaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"
                                )
                        );


        if (!transaction.getUserId().equals(userId)) {

            throw new UnauthorizedException(
                    "You cannot delete this transaction"
            );
        }


        transactionRepository.delete(transaction);

    }

}