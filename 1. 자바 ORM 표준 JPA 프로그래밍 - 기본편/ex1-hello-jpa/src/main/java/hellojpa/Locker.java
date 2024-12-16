package hellojpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Locker {

    @Id @GeneratedValue
    private Long id;

    private String name;

    // 일대일 - 읽기 전용
//    @OneToOne(mappedBy = "locker")
//    private Member member;

}
