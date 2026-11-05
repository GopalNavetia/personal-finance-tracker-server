package finance_tracker.service;

import finance_tracker.dto.*;
import finance_tracker.model.Category;
import finance_tracker.model.Transaction;
import finance_tracker.model.TransactionType;
import finance_tracker.repository.CategoryRepository;
import finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;

    private final CategoryRepository categoryRepository;


    public DashboardSummaryResponse getDashboardSummary(
            String userId
    ) {

        List<Transaction> transactions =
                transactionRepository.findByUserIdOrderByDateDesc(userId);


        double totalIncome = transactions.stream()
                .filter(transaction ->
                        transaction.getType() == TransactionType.INCOME
                )
                .mapToDouble(Transaction::getAmount)
                .sum();


        double totalExpense = transactions.stream()
                .filter(transaction ->
                        transaction.getType() == TransactionType.EXPENSE
                )
                .mapToDouble(Transaction::getAmount)
                .sum();


        double currentBalance =
                totalIncome - totalExpense;


        long transactionCount =
                transactionRepository.countByUserId(userId);


        long categoryCount =
                categoryRepository.countByUserId(userId);


        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .currentBalance(currentBalance)
                .transactionCount(transactionCount)
                .categoryCount(categoryCount)
                .build();

    }


    public List<MonthlyTrendResponse> getMonthlyTrends(
            String userId
    ) {


        LocalDate now = LocalDate.now();


        // Last 6 months range
        LocalDate startDate =
                now.minusMonths(5)
                        .withDayOfMonth(1);



        List<Transaction> transactions =
                transactionRepository
                        .findByUserIdOrderByDateDesc(userId);



        Map<String, MonthlyTrendResponse> monthlyData =
                new LinkedHashMap<>();



        // Create last 6 months with default values
        for(int i = 5; i >= 0; i--) {


            LocalDate month =
                    now.minusMonths(i);



            String monthName =
                    month.getMonth()
                            .toString();



            monthlyData.put(
                    monthName,

                    MonthlyTrendResponse.builder()
                            .month(monthName)
                            .income(0.0)
                            .expense(0.0)
                            .build()
            );

        }



        // Add transaction amounts
        for(Transaction transaction : transactions) {


            LocalDate transactionDate =
                    transaction.getDate();



            // Ignore old transactions
            if(transactionDate.isBefore(startDate)) {
                continue;
            }



            String monthName =
                    transactionDate.getMonth()
                            .toString();



            MonthlyTrendResponse data =
                    monthlyData.get(monthName);



            if(data == null) {
                continue;
            }



            if(transaction.getType()
                    == TransactionType.INCOME) {


                data.setIncome(
                        data.getIncome()
                                +
                                transaction.getAmount()
                );


            } else {


                data.setExpense(
                        data.getExpense()
                                +
                                transaction.getAmount()
                );

            }

        }



        return new ArrayList<>(
                monthlyData.values()
        );

    }


    public List<CategoryExpenseResponse> getCategoryExpenses(
            String userId
    ) {


        LocalDate now = LocalDate.now();


        List<Transaction> expenses =
                transactionRepository.findByUserIdAndType(
                        userId,
                        TransactionType.EXPENSE
                );


        Map<String, Double> categoryExpenses =
                new HashMap<>();



        for(Transaction transaction : expenses) {


            boolean currentMonth =
                    transaction.getDate()
                            .getMonth()
                            ==
                            now.getMonth()
                            &&
                            transaction.getDate()
                                    .getYear()
                                    ==
                                    now.getYear();



            if(!currentMonth) {
                continue;
            }



            Category category =
                    categoryRepository.findById(
                                    transaction.getCategoryId()
                            )
                            .orElse(null);



            if(category != null) {


                categoryExpenses.put(
                        category.getName(),

                        categoryExpenses.getOrDefault(
                                category.getName(),
                                0.0
                        )
                                +
                                transaction.getAmount()
                );

            }

        }



        return categoryExpenses.entrySet()
                .stream()
                .map(entry ->
                        CategoryExpenseResponse.builder()
                                .category(entry.getKey())
                                .amount(entry.getValue())
                                .build()
                )
                .toList();

    }

    public List<RecentTransactionResponse> getRecentTransactions(
            String userId
    ) {


        List<Transaction> transactions =
                transactionRepository
                        .findTop5ByUserIdOrderByDateDesc(userId);



        return transactions.stream()
                .map(transaction -> {


                    String categoryName = null;


                    if(transaction.getCategoryId() != null) {


                        Category category =
                                categoryRepository.findById(
                                                transaction.getCategoryId()
                                        )
                                        .orElse(null);


                        if(category != null) {
                            categoryName = category.getName();
                        }

                    }



                    return RecentTransactionResponse.builder()
                            .id(transaction.getId())
                            .type(transaction.getType().toString())
                            .amount(transaction.getAmount())
                            .category(categoryName)
                            .description(transaction.getDescription())
                            .date(transaction.getDate())
                            .build();

                })
                .toList();

    }

    public MonthlySummaryResponse getCurrentMonthSummary(
            String userId
    ) {


        LocalDate now = LocalDate.now();


        List<Transaction> transactions =
                transactionRepository.findByUserIdOrderByDateDesc(userId);



        double totalIncome =
                transactions.stream()
                        .filter(transaction ->
                                transaction.getDate().getMonth()
                                        == now.getMonth()
                        )
                        .filter(transaction ->
                                transaction.getType()
                                        == TransactionType.INCOME
                        )
                        .mapToDouble(Transaction::getAmount)
                        .sum();



        double totalExpense =
                transactions.stream()
                        .filter(transaction ->
                                transaction.getDate().getMonth()
                                        == now.getMonth()
                        )
                        .filter(transaction ->
                                transaction.getType()
                                        == TransactionType.EXPENSE
                        )
                        .mapToDouble(Transaction::getAmount)
                        .sum();



        return MonthlySummaryResponse.builder()
                .month(now.getMonth().toString())
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .remainingBalance(
                        totalIncome - totalExpense
                )
                .build();

    }

}