package finance_tracker.controller;


import finance_tracker.dto.TransactionRequest;
import finance_tracker.model.Transaction;
import finance_tracker.repository.UserRepository;
import finance_tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import finance_tracker.model.User;


import java.util.List;


@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {


    private final TransactionService transactionService;

    private final UserRepository userRepository;



    @PostMapping
    public ResponseEntity<Transaction> addTransaction(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        Transaction transaction =
                transactionService.addTransaction(
                        request,
                        user.getId()
                );


        return new ResponseEntity<>(
                transaction,
                HttpStatus.CREATED
        );
    }





    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        List<Transaction> transactions =
                transactionService.getTransactions(
                        user.getId()
                );


        return ResponseEntity.ok(transactions);

    }





    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(
            @PathVariable String id,
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        transactionService.deleteTransaction(
                id,
                user.getId()
        );


        return ResponseEntity.ok(
                "Transaction deleted successfully"
        );

    }

}