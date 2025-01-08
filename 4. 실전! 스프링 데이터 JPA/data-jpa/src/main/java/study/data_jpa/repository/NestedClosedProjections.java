package study.data_jpa.repository;

public interface NestedClosedProjections {  // 중첩구조

    String getUsername();
    TeamInfo getTeam();     // 두번째부터 최적화는 안됌. 다 갖고옴 Team

    interface TeamInfo {
        String getName();
    }
}
