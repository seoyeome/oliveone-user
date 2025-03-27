package com.oliveone.userservice.service;

import com.oliveone.userservice.dto.UserDto;
import com.oliveone.userservice.exception.UserNotFoundException;

public interface UserService {
    /**
     * 사용자 정보를 등록합니다.
     * @param request 등록할 사용자 정보 DTO
     * @return 등록된 사용자 정보 DTO
     * @throws com.example.userservice.exception.EmailAlreadyExistsException 이미 존재하는 이메일일 경우 발생
     */
    UserDto.UserInfoResponse registerUser(UserDto.UserRegistrationRequest request);

    /**
     * 사용자 ID로 사용자 정보를 조회합니다.
     * @param userId 조회할 사용자 ID
     * @return 조회된 사용자 정보 DTO
     * @throws UserNotFoundException 사용자를 찾을 수 없을 경우 발생
     */
    UserDto.UserInfoResponse getUserById(Long userId);
}
