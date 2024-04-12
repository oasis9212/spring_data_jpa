package study.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberJpaRepository;
import study.datajpa.repository.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
public class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    MemberRepository memberRepository;
    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(savedMember.getId());
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }

    @Test
    public void testMember2() {
        Member member = new Member("memberB");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }

    @Test
    public void basicCRUD(){
        Member member = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member);
        memberJpaRepository.save(member2);


        Member findmember = memberJpaRepository.findById(member.getId()).get();
        Member findmember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findmember).isEqualTo(member);
        assertThat(findmember2).isEqualTo(member2);

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(member);
        memberJpaRepository.delete(member2);

        long deletecount = memberJpaRepository.count();
        assertThat(deletecount).isEqualTo(0);
    }


    @Test
    public void testNamedQuery(){
        Member member1=new Member("Member1",10 );
        Member member2=new Member("Member2",20 );
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUsername("Member1");
        Member findmember= result.get(0);
        assertThat(findmember).isEqualTo(result);
    }


    @Test
    public  void paging(){
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        memberJpaRepository.save(new Member("member6", 10));


      List<Member> m2=  memberJpaRepository.findByPage(10,1,3);
      Long count = memberJpaRepository.totalcount(10);
      m2.stream().forEach( e -> System.out.println(e.toString()));
        System.out.println(count);
    }


    @Test
    public void bulkUpdate(){
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 11));
        memberJpaRepository.save(new Member("member3", 12));
        memberJpaRepository.save(new Member("member4", 13));
        memberJpaRepository.save(new Member("member5", 14));
        memberJpaRepository.save(new Member("member6", 15));

        int result=memberJpaRepository.bulkAgePlus(13);
        assertThat(result).isEqualTo(3);
    }

}
