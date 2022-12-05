package com.ssafy.dokcho2.service.user;


import com.ssafy.dokcho2.dto.jwt.TokenDto;
import com.ssafy.dokcho2.dto.jwt.TokenRequestDto;
import com.ssafy.dokcho2.dto.user.LoginRequestDto;
import com.ssafy.dokcho2.dto.user.SignUpRequestDto;
import com.ssafy.dokcho2.dto.user.UserResponseDto;

public interface UserService {

    boolean checkEmail(String email);
    boolean checkUsername(String username);
    boolean checkNickName(String nickname);
    boolean checkPW(Long id, String nowPW);
    boolean checkSameUser(String email, String username);
    TokenDto doLogin(LoginRequestDto requestDto);
    UserResponseDto doSignUp(SignUpRequestDto requestDto);
    void deleteUser(Long id);
    void changePW(String email, String newPW);
    void changePW(Long id, String newPW);
    void setNickname(String nickname);
    UserResponseDto getMyInfo();

    UserResponseDto getUserInfo(Long userId);
    TokenDto refresh(TokenRequestDto requestDto);
    void logout();
    UserResponseDto search(String keyword);

    void changeRepresentMonster(Long monsterId);

    void reset();
}
