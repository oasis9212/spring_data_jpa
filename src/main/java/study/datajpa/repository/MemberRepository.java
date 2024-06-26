package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom {
    List<Member> findByUsername(String username);

    // 관려상 규칙 으로 제작 가능하다.
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    //  관리 상 규칙은 여기 참조
    List<Member> findByUsernameAndAgeGreaterThan(String username,int age);

    List<Member> findTop3HelloBy();

    // 사실 잘안쓰임 너무 짜증남
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername2(@Param("username") String username);

    // 이방법을 많이 쓰임

    @Query("select m from Member m where m.username = :username  and m.age = :age")
    List<Member> findByUsernameAndAge(@Param("username") String username,@Param("age") int age);


    @Query("select m.username from  Member m ")
    List<String>  findUsernameList();


    @Query("select new study.datajpa.dto.MemberDto( m.id, m.username, t.name )  from Member m join m.team t ")
    List<MemberDto> findMemberDto();

    // 관호 쉽표를 자동으로 처리한다.
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> namess);

    List<Member> findListByusername(String username); // 컬랫션
    Member findMemberByUsername(String username); // 단건

    Optional<Member> findOptionalByUsername(String username); // 단건 옵셔널


   // Slice<Member> findByAge(int age, Pageable pageable);
    // 카운트 쿼리를 따로 분리 하는 방법.
    //
    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberfetchjointeam();


    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
    // 성능 최적화형 겸 소스 줄이기.
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberAndTeam();


    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    List<Member> findMemberCustom();

    <T> List<T> findProjectionsByUsername(@Param("username")String username, Class<T> type);

    @Query(value = "select m.member_id as id, m.username, t.name as teamName " +
            "from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByProjectionQuery(Pageable pageable);
}
