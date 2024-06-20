package com.sparta.delivery_app.domain.menu.service;

import com.sparta.delivery_app.domain.user.adaptor.UserAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final UserAdaptor userAdaptor;

    /**
     * 방법 2
     * @param email
     */
    public void test2(String email) {
        userAdaptor.checkDuplicateEmail(email);
    }








}
