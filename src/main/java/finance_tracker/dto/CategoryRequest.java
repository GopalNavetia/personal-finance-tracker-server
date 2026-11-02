package finance_tracker.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {


    @NotBlank(message = "Category name is required")
    private String name;


    @NotNull(message = "Budget is required")
    @Positive(message = "Budget must be greater than 0")
    private Double budget;

}