package com.sparta.delivery_app.domain.user.service;

import com.sparta.delivery_app.common.globalcustomexception.UserDuplicatedException;
import com.sparta.delivery_app.domain.user.adaptor.UserAdaptor;
import com.sparta.delivery_app.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sparta.delivery_app.common.exception.errorcode.UserErrorCode.DUPLICATED_USER;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository; //방법1
    private final UserAdaptor userAdaptor; //방법2


    /**
     * 어댑터보다 더 많은 줄이 발생함. 또한 검증 로직은 다른 도메인에서도 사용하는 경우 의존성 주입의 문제도 생김.
     */
    public void signupAdd(String email) {
        //방법1
        userRepository.findByEmail(email).ifPresent(u ->
                {
                    throw new UserDuplicatedException(DUPLICATED_USER);
                }
        );

        //방법 2
        //검증
        userAdaptor.checkDuplicateEmail(email);

//        return UserSignupResponseDto.of(user ~~~~)
    }
}
