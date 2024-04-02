package study.datajpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity(){
       Team team1= new Team("team1");
       Team team2= new Team("team2");
       em.persist(team2);
       em.persist(team1);

        Member member1=new Member("Member1",10 , team1);
        Member member2=new Member("Member2",20 , team1);
        Member member3=new Member("Member3",30 , team2);
        Member member4=new Member("Member4",40 , team2);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member>  members = em.createQuery("select m from Member m", Member.class).getResultList();

        for(Member member: members){
            System.out.println("member - > "+member);
            System.out.println("member Team - > "+member.getTeam());
        }



    }

}
