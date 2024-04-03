package study.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
@Rollback(false)
public class MemeberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void basicCRUD(){
        Member member = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member);
        memberRepository.save(member2);


        Member findmember = memberRepository.findById(member.getId()).get();
        Member findmember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findmember).isEqualTo(member);
        assertThat(findmember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member);
        memberRepository.delete(member2);

        long deletecount = memberRepository.count();
        assertThat(deletecount).isEqualTo(0);
    }
}
