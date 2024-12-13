package hellojpa;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",    // 매핑할 데이터베이스 시퀀스 이름
        initialValue = 1, allocationSize = 50           //  persist 때마다 DB 다녀오면 성능 문제 발생 -> allocationSize 미리 땡겨놓음
)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "name")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
