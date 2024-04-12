package study.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
@Rollback(false)
public class MemeberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @Autowired
    TeamRepository teamRepository;

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


    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member member = new Member("AAA",10);
        Member member2 = new Member("AAA",20);
        memberRepository.save(member);
        memberRepository.save(member2);

        List<Member> list= memberRepository.findByUsernameAndAgeGreaterThan("AAA",15);

        assertThat(list.get(0).getUsername()).isEqualTo("AAA");
        assertThat(list.get(0).getAge()).isEqualTo(20);
        assertThat(list.size()).isEqualTo(1);

    }
    @Test
    public void findheelo(){
        List<Member> list = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNamedQuery2(){
        Member member1=new Member("Member1",10 );
        Member member2=new Member("Member2",20 );
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername2("Member1");
        Member findmember= result.get(0);
        assertThat(findmember).isEqualTo(result);

        //findByUsernameAndAge
    }


    @Test
    public void testNamedQuery3(){
        Member member1=new Member("Member1",10 );
        Member member2=new Member("Member2",20 );
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAge("Member1",10);

        assertThat(result.get(0)).isEqualTo(member1);

        List<String> Memberlist= memberRepository.findUsernameList();

        Memberlist.stream().forEach(e -> System.out.println(e));

        //findByUsernameAndAge
    }

    @Test
    public void testQuery5(){
        Team team1= new Team("team1");
        Team team2= new Team("team2");
        teamRepository.save(team2);
        teamRepository.save(team1);

        Member member1=new Member("Member1",10 , team1);
        Member member2=new Member("Member2",20 , team1);
        Member member3=new Member("Member3",30 , team2);
        Member member4=new Member("Member4",40 , team2);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);


        List<MemberDto>  MemberDto = memberRepository.findMemberDto();

        MemberDto.stream().forEach( e -> System.out.println("Id ::"+e.getId()+" Username :: "+e.getUsername() +" Teamname :: "+e.getTeamname()));


    }

    @Test
    public void QueryBynames(){
        Member member1=new Member("Member1",10 );
        Member member2=new Member("Member2",20 );
        Member member3=new Member("Member3",30 );
        Member member4=new Member("Member4",40 );
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> list= memberRepository.findByNames(Arrays.asList("Member1","Member2"));

    }

    @Test
    public void findBynames(){

        Member member1=new Member("Member1",10 );
        Member member2=new Member("Member2",20 );
        Member member3=new Member("Member3",30 );
        Member member4=new Member("Member4",40 );
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> list=  memberRepository.findListByusername("Meember1");
        Member membername=  memberRepository.findMemberByUsername("Member2");
        Optional<Member> optionalMember= memberRepository.findOptionalByUsername("Member3");

    }

    @Test
    public  void paging1(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        PageRequest request = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> m2=  memberRepository.findByAge(10,request);

        Page<MemberDto> MemberDtomap=  m2.map(member -> new MemberDto(member.getId(), member.getUsername() , null));
        // 해당 리스트를 가져오고 싶다면  getContent
        List<Member> content = m2.getContent();
        content.stream().forEach(e -> System.out.println(e.toString()));

        System.out.println(m2.getTotalElements()+":: getTotalElements"); // totalcount 값을 가져온다.
        // 페이지의 번호 가져오기.   사이즈가 3이니깐  0
        System.out.println(m2.getNumber()+":: getNumber");
        // 총페이지 갯수
        System.out.println(m2.getTotalPages()+":: getTotalPages");

        System.out.println(m2.isFirst()+":: isFirst");
        System.out.println(m2.hasNext()+":: getTotalPages");

    }

//    @Test
//    public  void paging2() {
//        memberRepository.save(new Member("member1", 10));
//        memberRepository.save(new Member("member2", 10));
//        memberRepository.save(new Member("member3", 10));
//        memberRepository.save(new Member("member4", 10));
//        memberRepository.save(new Member("member5", 10));
//        memberRepository.save(new Member("member6", 10));
//
//        PageRequest request = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "username"));
//        // 슬라이스는 페이지 사이즈 에  +1 더해서 가져온다.
//        Slice<Member> m2=  memberRepository.findByAge(10,request);
//
//    //    System.out.println(m2.getTotalElements()+":: getTotalElements"); // totalcount 값을 가져온다.
//
//        System.out.println(m2.getNumber()+":: getNumber");
//
//  //      System.out.println(m2.getTotalPages()+":: getTotalPages");
//
//        System.out.println(m2.isFirst()+":: isFirst");
//        System.out.println(m2.hasNext()+":: getTotalPages");
//
//    }


    @Test
    public void bulkUpdate(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 11));
        memberRepository.save(new Member("member3", 12));
        memberRepository.save(new Member("member4", 13));
        memberRepository.save(new Member("member5", 14));
        memberRepository.save(new Member("member6", 15));

        int result=memberRepository.bulkAgePlus(13);

        List<Member> re=memberRepository.findByUsername("member5");
        Member member= re.get(0);// 그대로 15살 영속성으로 유지되어 있다. OSIV 참조

        assertThat(result).isEqualTo(3);
    }
}
