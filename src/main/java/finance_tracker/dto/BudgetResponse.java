package finance_tracker.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponse {


    private String categoryId;

    private String categoryName;

    private Double budget;

    private Double spent;

    private Double remaining;

}