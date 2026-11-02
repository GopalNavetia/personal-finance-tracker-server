package finance_tracker.controller;


import finance_tracker.dto.BudgetResponse;
import finance_tracker.exception.ResourceNotFoundException;
import finance_tracker.model.User;
import finance_tracker.repository.UserRepository;
import finance_tracker.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/budget-summary")
@RequiredArgsConstructor
public class BudgetController {


    private final BudgetService budgetService;

    private final UserRepository userRepository;



    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgetSummary(
            Authentication authentication
    ) {


        String email = authentication.getName();



        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );



        List<BudgetResponse> summary =
                budgetService.getBudgetSummary(
                        user.getId()
                );


        return ResponseEntity.ok(summary);

    }

}