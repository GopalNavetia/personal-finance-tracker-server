package finance_tracker.service;

import finance_tracker.dto.AuthResponse;
import finance_tracker.dto.LoginRequest;
import finance_tracker.dto.RegisterRequest;
import finance_tracker.exception.EmailAlreadyExistsException;
import finance_tracker.exception.ResourceNotFoundException;
import finance_tracker.model.User;
import finance_tracker.repository.UserRepository;
import finance_tracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already registered"
            );
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .message("User registered successfully")
                .build();
    }


    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);


        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found"
            );
        }


        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new ResourceNotFoundException(
                    "Invalid email or password"
            );
        }


        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .message("Login successful")
                .build();
    }
}