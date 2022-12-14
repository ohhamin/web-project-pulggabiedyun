package com.ssafy.dokcho2.service.user;

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
import com.ssafy.dokcho2.domain.userItem.UserItemRepository;
import com.ssafy.dokcho2.domain.userMonster.UserMonster;
import com.ssafy.dokcho2.domain.userMonster.UserMonsterRepository;
import com.ssafy.dokcho2.dto.exception.mission.MissionNotFoundException;
import com.ssafy.dokcho2.dto.exception.monster.MonsterNotFoundException;
import com.ssafy.dokcho2.dto.exception.user.DuplicateEmailException;
import com.ssafy.dokcho2.dto.exception.user.DuplicateNicknameException;
import com.ssafy.dokcho2.dto.exception.user.DuplicateUsernameException;
import com.ssafy.dokcho2.dto.exception.user.UserNotFoundException;
import com.ssafy.dokcho2.dto.jwt.TokenDto;
import com.ssafy.dokcho2.dto.jwt.TokenRequestDto;
import com.ssafy.dokcho2.dto.user.LoginRequestDto;
import com.ssafy.dokcho2.dto.user.SignUpRequestDto;
import com.ssafy.dokcho2.dto.user.UserResponseDto;
import com.ssafy.dokcho2.jwt.TokenProvider;
import com.ssafy.dokcho2.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final UserMissionRepository userMissionRepository;
    private final MissionRepository missionRepository;
    private final MonsterRepository monsterRepository;
    private final UserMonsterRepository userMonsterRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserItemRepository userItemRepository;

    @Override
    public boolean checkEmail(String email) {
        // ?????? ????????? true, ????????? false
        Optional<User> entity = userRepository.findByEmail(email);

        return entity.isPresent();
    }

    @Override
    public boolean checkUsername(String username){
        Optional<User> entity = userRepository.findByUsername(username);

        return entity.isPresent();
    }

    @Override
    public boolean checkNickName(String nickname) {
        // ?????? ????????? true, ????????? false
        Optional<User> entity = userRepository.findByNickname(nickname);

        return entity.isPresent();
    }

    @Override
    public boolean checkPW(Long id, String nowPW){
        String username = userRepository.findById(id).orElseThrow(UserNotFoundException::new).getUsername();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, nowPW);

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }

    @Override
    public boolean checkSameUser(String email, String username){
        Long emailId = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
        Long nameId = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new).getUserId();

        return emailId == nameId;
    }

    @Override
    public TokenDto doLogin(LoginRequestDto requestDto) {
        // Login id/pw??? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword());

        // ?????? ??????
        // CustomUserDetailsService??? loadByUserName ??????
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ?????? ????????? ???????????? JWT ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // Refresh Token ??????
        Optional<User> entity = userRepository.findByUsername(authentication.getName());

        if(entity.isPresent()){
            entity.get().saveToken(tokenDto.getRefreshToken());
            userRepository.save(entity.get());
        }

        // ?????? ??????
        return tokenDto;
    }

    @Override
    public UserResponseDto doSignUp(SignUpRequestDto requestDto) {

        if(userRepository.findByUsername(requestDto.getUsername()).orElse(null)!=null){
            throw new DuplicateUsernameException();
        }

        if(userRepository.findByEmail(requestDto.getEmail()).orElse(null)!=null){
            throw new DuplicateEmailException();
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .nickname("")
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .representMonster(monsterRepository.findById((long)1).orElseThrow(MonsterNotFoundException::new))
                .role(Role.ROLE_USER)
                //?????? ???????????? ?????? id ??????????????? 1???
                .nowMissionId((long)1)
                .build();
        userRepository.save(user);

        // ??????-?????? ???????????? 8??? ?????? ??????
        for(int i=1; i<=8; i++){
            Mission mission = missionRepository.findById((long)i).orElseThrow(MissionNotFoundException::new);
            UserMission um;
            //????????? ?????? ????????? READY??? ??????
            if (i == 1) {
                um = UserMission.builder()
                        .user(user)
                        .mission(mission)
                        .status(MissionStatus.READY)
                        .build();
            } else {
                um = UserMission.builder()
                        .user(user)
                        .mission(mission)
                        .status(MissionStatus.NOT_YET)
                        .build();
            }
            userMissionRepository.save(um);
        }

        // ?????? ????????? ??????
        Monster monster = monsterRepository.findById((long)1).orElseThrow(MonsterNotFoundException::new);
        UserMonster um = UserMonster.builder()
                .user(user)
                .monster(monster)
                .build();

        userMonsterRepository.save(um);

        return UserResponseDto.from(user);
    }


    @Override
    public UserResponseDto getMyInfo(){
        return UserResponseDto.from(SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new));
    }


    @Override
    public UserResponseDto getUserInfo(Long userId) {
        return UserResponseDto.from(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public void deleteUser(Long id){
        Optional<User> entity = userRepository.findById(id);

        if(entity.isPresent()){
            userRepository.delete(entity.get());
            return;
        }

        throw new UserNotFoundException();
    }

    @Override
    public void changePW(String email, String newPW) {
        Optional<User> entity = userRepository.findByEmail(email);

        if(entity.isPresent()){
            entity.get().changePW(newPW);
            return;
        }

        throw new UserNotFoundException();
    }

    @Override
    public void changePW(Long id, String newPW){
        Optional<User> entity = userRepository.findById(id);

        if(entity.isPresent()){
            entity.get().changePW(newPW);
            return;
        }

        throw new UserNotFoundException();
    }

    @Override
    public void setNickname(String nickname){
        if(checkNickName(nickname)) throw new DuplicateNicknameException();

        User me = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        me.setNickname(nickname);
        userRepository.save(me);
    }

    @Override
    public TokenDto refresh(TokenRequestDto requestDto){
        // Refresh Token ??????
        if(!tokenProvider.validateToken(requestDto.getRefreshToken())){
            throw new RuntimeException("Refresh Token??? ???????????? ????????????.");
        }

        // Access Token?????? Id(username) ????????????
        Authentication authentication = tokenProvider.getAuthentication(requestDto.getAccessToken());

        // ????????? ID??? Refresh Token ????????????
        User entity = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()->new RuntimeException("??????????????? ??????????????????."));

        String refreshToken = entity.getToken();

        // ?????? ??????
        if(!refreshToken.equals(requestDto.getRefreshToken())){
            throw new RuntimeException("????????? ?????? ????????? ???????????? ????????????.");
        }

        // ??? ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // DB ?????? ????????????
        entity.saveToken(tokenDto.getRefreshToken());
        userRepository.save(entity);

        // ?????? ??????
        return tokenDto;
    }

    @Override
    public void logout(){
        Optional<User> entity = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername);

        if(entity.isPresent()){
            entity.get().saveToken("");
            userRepository.save(entity.get());
            return;
        }

        throw new RuntimeException("??????????????? ??????????????????.");
    }

    @Override
    public UserResponseDto search(String keyword) {
        User user = userRepository.findByNickname(keyword).orElseThrow(UserNotFoundException::new);

        return UserResponseDto.from(user);
    }

    @Override
    @Transactional
    public void changeRepresentMonster(Long monsterId) {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);

        Monster monster = monsterRepository.findById(monsterId).orElseThrow(MonsterNotFoundException::new);

        Optional<UserMonster> o = userMonsterRepository.findByUserAndMonster(user, monster);

        if (o.isPresent()){
            user.changeRepresentMonster(monster);
            userRepository.save(user);
        } else {
            throw new MonsterNotFoundException();
        }
    }

    @Override
    public void reset() {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(UserNotFoundException::new);
        userMissionRepository.deleteAllByUser(user);
        userMonsterRepository.deleteAllByUser(user);
        userItemRepository.deleteAllByUser(user);

        // ??????-?????? ???????????? 8??? ?????? ??????
        for(int i=1; i<=8; i++){
            Mission mission = missionRepository.findById((long)i).orElseThrow(MissionNotFoundException::new);
            UserMission um;
            //????????? ?????? ????????? READY??? ??????
            if (i == 1) {
                um = UserMission.builder()
                        .user(user)
                        .mission(mission)
                        .status(MissionStatus.READY)
                        .build();
            } else {
                um = UserMission.builder()
                        .user(user)
                        .mission(mission)
                        .status(MissionStatus.NOT_YET)
                        .build();
            }
            userMissionRepository.save(um);
            user.changeNowMissionId((long)1);
            userRepository.save(user);
        }

        // ?????? ????????? ??????
        Monster monster = monsterRepository.findById((long)1).orElseThrow(MonsterNotFoundException::new);
        UserMonster um = UserMonster.builder()
                .user(user)
                .monster(monster)
                .build();

        userMonsterRepository.save(um);
        log.info("?????? ????????? ???..");
        log.info("?????? ????????? & ????????? ????????? ??????");
        user.changeRepresentMonster(monsterRepository.findById((long)1).orElseThrow(MonsterNotFoundException::new));
        log.info("?????? ????????? 1????????? ?????? ??????");
    }
}
