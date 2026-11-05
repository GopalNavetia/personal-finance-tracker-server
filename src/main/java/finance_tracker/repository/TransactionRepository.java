package finance_tracker.repository;

import finance_tracker.model.Transaction;
import finance_tracker.model.TransactionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository
        extends MongoRepository<Transaction, String> {

    List<Transaction> findByUserIdOrderByDateDesc(String userId);

    List<Transaction> findByUserIdAndCategoryId(
            String userId,
            String categoryId
    );

    long countByUserId(String userId);

    List<Transaction> findByUserIdAndType(
            String userId,
            TransactionType type
    );

    List<Transaction> findTop5ByUserIdOrderByDateDesc(
            String userId
    );
}