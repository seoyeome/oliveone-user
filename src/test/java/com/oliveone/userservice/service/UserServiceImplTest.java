package com.oliveone.userservice.service;

import com.oliveone.userservice.domain.User;
import com.oliveone.userservice.dto.UserDto;
import com.oliveone.userservice.exception.EmailAlreadyExistsException;
import com.oliveone.userservice.exception.UserNotFoundException;
import com.oliveone.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto.UserRegistrationRequest registrationRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registrationRequest = new UserDto.UserRegistrationRequest();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setName("Test User");

        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .name("Test User")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("회원 가입 성공")
    void registerUser_Success() {
        // given
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        UserDto.UserInfoResponse response = userService.registerUser(registrationRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(registrationRequest.getEmail());
        assertThat(response.getName()).isEqualTo(registrationRequest.getName());

        // Mock 객체의 메소드 호출 검증
        then(userRepository).should().existsByEmail(registrationRequest.getEmail());
        then(passwordEncoder).should().encode(registrationRequest.getPassword());
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("회원 가입 실패 - 이메일 중복")
    void registerUser_Fail_EmailExists() {
        // given
        given(userRepository.existsByEmail(anyString())).willReturn(true); // 이메일 중복 있음

        // when & then
        //isInstanceOf: 예외 타입 검증
        assertThatThrownBy(() -> userService.registerUser(registrationRequest))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");

        // Mock 객체 메소드 호출 검증 (save나 encode는 호출되면 안 됨)
        //never(): 메소드가 호출되지 않았는지 검증
        then(userRepository).should().existsByEmail(registrationRequest.getEmail());
        then(passwordEncoder).should(never()).encode(anyString());
        then(userRepository).should(never()).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 ID로 조회 성공")
    void getUserById_Success() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user)); // 조회 결과 설정

        // when
        UserDto.UserInfoResponse response = userService.getUserById(userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(userId);
        assertThat(response.getEmail()).isEqualTo(user.getEmail());

        then(userRepository).should().findById(userId);
    }

    @Test
    @DisplayName("사용자 ID로 조회 실패 - 사용자를 찾을 수 없음")
    void getUserById_Fail_NotFound() {
        // given
        Long userId = 99L;
        given(userRepository.findById(userId)).willReturn(Optional.empty()); // 조회 결과 없음

        // when & then
        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");

        then(userRepository).should().findById(userId);
    }

}