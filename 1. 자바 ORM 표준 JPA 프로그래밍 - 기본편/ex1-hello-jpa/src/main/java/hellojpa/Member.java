package hellojpa;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Member {

    @Id
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;

    private Integer age;

    // default ordinal : 숫자로 DB에 저장됨(운영상 위험 : 변경될 경우 값의 무결성을 보호하지 않음)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // 지금은 LocalDate, LocalDateTime 권장
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // 문자면 CLOB, 숫자면 BLOB 으로 매핑
    @Lob
    private String description;

    // 매핑 안할경
   @Transient
    private int temp;

    public Member() {
    }
}
