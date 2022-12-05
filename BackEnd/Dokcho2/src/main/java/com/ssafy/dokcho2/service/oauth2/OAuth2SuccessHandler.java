package com.ssafy.dokcho2.service.oauth2;

import com.ssafy.dokcho2.domain.enums.MissionStatus;
import com.ssafy.dokcho2.domain.enums.Role;
import com.ssafy.dokcho2.domain.mission.Mission;
import com.ssafy.dokcho2.domain.mission.MissionRepository;
import com.ssafy.dokcho2.domain.mission.UserMission;
import com.ssafy.dokcho2.domain.mission.UserMissionRepository;
import com.ssafy.dokcho2.domain.monster.Monster;
import com.ssafy.dokcho2.domain.monster.MonsterRepository;
import com.ssafy.dokcho2.domain.user.User;
import com.ssafy.dokcho2.domain.user.UserRepository;
import com.ssafy.dokcho2.domain.userMonster.UserMonster;
import com.ssafy.dokcho2.domain.userMonster.UserMonsterRepository;
import com.ssafy.dokcho2.dto.exception.mission.MissionNotFoundException;
import com.ssafy.dokcho2.dto.exception.monster.MonsterNotFoundException;
import com.ssafy.dokcho2.dto.jwt.TokenDto;
import com.ssafy.dokcho2.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserMissionRepository userMissionRepository;
    private final MissionRepository missionRepository;
    private final MonsterRepository monsterRepository;
    private final UserMonsterRepository userMonsterRepository;
    private final PasswordMaker passwordMaker;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();


        if(attributes.get("kakao_account")!=null){
            attributes = (Map<String, Object>) attributes.get("kakao_account");
        }

        String email = (String) attributes.get("email");

        // db 확인
        // 새로운 유저 -> 회원가입
        Optional<User> entity = userRepository.findByEmail(email);
        TokenDto tokenDto = new TokenDto();

        if(!entity.isPresent()){
            User user = User.builder().username(email)
                    .password(passwordMaker.make())
                    .email(email)
                    .nickname("")
                    .representMonster(monsterRepository.findById((long)1).orElseThrow(MonsterNotFoundException::new))
                    .role(Role.ROLE_USER)
                    .build();

            // 토큰 생성
            tokenDto = tokenProvider.generateTokenDto(user.getUsername(), user.getRole().toString());
            user.saveToken(tokenDto.getRefreshToken());
            userRepository.save(user);

            // 유저-미션 테이블에 8개 넣는 코드
            for(int i=1; i<=8; i++){
                Mission mission = missionRepository.findById((long)i).orElseThrow(MissionNotFoundException::new);
                UserMission um = UserMission.builder()
                        .user(user)
                        .mission(mission)
                        .status(MissionStatus.NOT_YET)
                        .build();

                userMissionRepository.save(um);
            }

            // 기본 풀깨비 지급
            for(int i=1; i<=3; i++){
                Monster monster = monsterRepository.findById((long)i).orElseThrow(MonsterNotFoundException::new);
                UserMonster um = UserMonster.builder()
                        .user(user)
                        .monster(monster)
                        .build();

                userMonsterRepository.save(um);
            }
        }else {
            // 토큰 생성
            tokenDto = tokenProvider.generateTokenDto(entity.get().getUsername(), entity.get().getRole().toString());
            entity.get().saveToken(tokenDto.getRefreshToken());
            userRepository.save(entity.get());
        }

        // 리다이렉트
        String target = "https://k7e203.p.ssafy.io/oauth?Auth=" + tokenDto.getAccessToken() + "&Refresh=" + tokenDto.getRefreshToken();
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, target);
    }
}
