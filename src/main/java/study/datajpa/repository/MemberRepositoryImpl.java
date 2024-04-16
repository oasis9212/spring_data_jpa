package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import java.util.List;

// 규칙을 인터페이스로 맞춰 주며 + Impl 로 해줘야한다.
// MemberRepository
@RequiredArgsConstructor
public class MemberRepositoryImpl  implements  MemberRepositoryCustom{

    private final EntityManager em;


    @Override
    public List<Member> findMemberCustom() {

        return em.createQuery("select m from Member m").getResultList();
    }
}
