package finance_tracker.controller;


import finance_tracker.dto.UserResponse;
import finance_tracker.exception.ResourceNotFoundException;
import finance_tracker.model.User;
import finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserRepository userRepository;


    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication
    ) {


        String email = authentication.getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        UserResponse response =
                UserResponse.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .build();


        return ResponseEntity.ok(response);

    }

}