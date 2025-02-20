package study.data_jpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    /* 메소드 이름으로 쿼리 생성 */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();


    /* NamedQuery */
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);


    /* @query, 리포지토리 메소드에 쿼리 정의하기 */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);


    /* @query, 값, DTO 조회하기 */
    @Query("select m.username from Member m")
    List<String> findByUsernameList();

    @Query("select new study.data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();


    /* 파라미터 바인딩(이름기반) */
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    /* 반환 기능 (컬렉션, 단건, 단건 Optional) */
    List<Member> findListByUsername(String username);   // 컬렉션

    Member findMemberByUsername(String username);   // 단건

    Optional<Member> findOptionalByUsername(String username);   // 단건 Optional


    /* 스프링 테이터 JPA 페이징, 정렬 */
    Page<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findCountByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);


    /* 벌크성 수정 쿼리 */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age * 10 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    /* @EntityGraph */
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    @Query("select m from Member m where m.username = :username")
    List<Member> findEntityGraphByUsername(@Param("username") String username);


    /* JPA Hint & Lock */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);


    /* Projections */
    List<NestedClosedProjections> findProjectionsByUsername(@Param("username")String username);


    /* native Query */
    @NativeQuery(value = "select * from member where username = ?")
    Member findByNativeQuery(String username);

    @NativeQuery(value = "select m.member_id as id, m.username, t.name as teamName " +
                         " from member m left join team t",
                countQuery = "select count(*) from member")
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}