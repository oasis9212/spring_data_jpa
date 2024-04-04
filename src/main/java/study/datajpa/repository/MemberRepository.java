package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByUsername(String username);

    // 관려상 규칙 으로 제작 가능하다.
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    //  관리 상 규칙은 여기 참조
    List<Member> findByUsernameAndAgeGreaterThan(String usernane,int age);

    List<Member> findTop3HelloBy();
}
