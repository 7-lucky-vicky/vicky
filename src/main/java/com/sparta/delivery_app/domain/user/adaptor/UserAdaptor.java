package com.sparta.delivery_app.domain.user.adaptor;

import com.sparta.delivery_app.common.globalcustomexception.UserDuplicatedException;
import com.sparta.delivery_app.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.sparta.delivery_app.common.exception.errorcode.UserErrorCode.DUPLICATED_USER;

@Component
@RequiredArgsConstructor
public class UserAdaptor {

    private final UserRepository userRepository;

    public void checkDuplicateEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(u -> {
                            throw new UserDuplicatedException(DUPLICATED_USER);
                        }
                );
    }
}
