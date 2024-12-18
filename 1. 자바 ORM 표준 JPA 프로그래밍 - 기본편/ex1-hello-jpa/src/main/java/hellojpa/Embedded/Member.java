package hellojpa.Embedded;

import hellojpa.BaseEntity;
import jakarta.persistence.*;

@Entity(name = "Member2")
@Table(name = "Member2")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    // 기간
    private Period period;

    // 주소
    private Address address;

    public Member() {

    }

    public Member(Long id, String username, Period period, Address address) {
        this.id = id;
        this.username = username;
        this.period = period;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Period getPeriod() {
        return period;
    }

    public Address getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}