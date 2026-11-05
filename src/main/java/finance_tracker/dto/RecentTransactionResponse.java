package finance_tracker.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentTransactionResponse {


    private String id;

    private String type;

    private Double amount;

    private String category;

    private String description;

    private LocalDate date;

}