package com.oliveone.userservice.service;

import com.oliveone.userservice.domain.User;
import com.oliveone.userservice.dto.UserDto;
import com.oliveone.userservice.exception.ErrorType;
import com.oliveone.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto.UserInfoResponse registerUser(UserDto.UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw ErrorType.EMAIL_ALREADY_EXISTS.throwException(request.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
            .email(request.getEmail())
            .password(encodedPassword)
            .name(request.getName())
            .build();

        User savedUser = userRepository.save(user);

        return UserDto.UserInfoResponse.fromEntity(savedUser);
    }

    @Override
    public UserDto.UserInfoResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> ErrorType.USER_NOT_FOUND.throwException(userId));

        return UserDto.UserInfoResponse.fromEntity(user);
    }
}
