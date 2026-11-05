package finance_tracker.controller;

import finance_tracker.dto.*;
import finance_tracker.exception.ResourceNotFoundException;
import finance_tracker.model.User;
import finance_tracker.repository.UserRepository;
import finance_tracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    private final UserRepository userRepository;


    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        DashboardSummaryResponse response =
                dashboardService.getDashboardSummary(
                        user.getId()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly-trends")
    public ResponseEntity<List<MonthlyTrendResponse>> getMonthlyTrends(
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        List<MonthlyTrendResponse> response =
                dashboardService.getMonthlyTrends(
                        user.getId()
                );


        return ResponseEntity.ok(response);

    }

    @GetMapping("/category-expense")
    public ResponseEntity<List<CategoryExpenseResponse>> getCategoryExpense(
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        List<CategoryExpenseResponse> response =
                dashboardService.getCategoryExpenses(
                        user.getId()
                );


        return ResponseEntity.ok(response);

    }

    @GetMapping("/recent-transactions")
    public ResponseEntity<List<RecentTransactionResponse>> getRecentTransactions(
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        List<RecentTransactionResponse> response =
                dashboardService.getRecentTransactions(
                        user.getId()
                );


        return ResponseEntity.ok(response);

    }

    @GetMapping("/month-summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthSummary(
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        MonthlySummaryResponse response =
                dashboardService.getCurrentMonthSummary(
                        user.getId()
                );


        return ResponseEntity.ok(response);

    }
}