package com.sparta.delivery_app;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestDB {

    private final InitService initService;


    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
//        @Autowired
//        PasswordEncoder passwordEncoder;

        public void dbInit1() {

//            save(User.builder()
//                    .password(passwordEncoder.encode("password"))
//                    .build());
        }

        public void save(Object... objects) {
            for (Object object : objects) {
                em.persist(object);
            }
        }
    }
}

