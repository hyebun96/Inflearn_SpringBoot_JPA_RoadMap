package jpabook.jpashop.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 총 주문 2개
 *  * userA
 *      * JPA1 Book
 *      * JPA2 Book
 *  * userB
 *      * SPRING1 Book
 *      * SPRING2 Book
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

}
