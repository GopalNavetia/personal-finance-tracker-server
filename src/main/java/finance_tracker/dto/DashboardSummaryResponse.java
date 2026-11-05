package finance_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    private Double totalIncome;

    private Double totalExpense;

    private Double currentBalance;

    private Long transactionCount;

    private Long categoryCount;

}