package finance_tracker.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;


    @NotBlank(message = "Name is required")
    private String name;

    @Indexed(unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;


    @NotBlank(message = "Password is required")
    private String password;
}