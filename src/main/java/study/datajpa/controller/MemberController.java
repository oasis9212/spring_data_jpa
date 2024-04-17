package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;


    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member= memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 중간에 회원 객체로 조회한다.
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){

        return member.getUsername();
    }
    // PageableDefault 일부 설정.
    // page 는 0 부터 시작한다.
    // page 새로 정의할라면..,
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){
       //  PageRequest request=  PageRequest.of(1,2); ㅇ;럴경우 새로 정의된다.
        Page<Member> page=memberRepository.findAll(pageable);
        Page<MemberDto> page2=  page.map(member -> new MemberDto(member));
        return page2;
    }


    @PostConstruct
    public  void init(){

        for(int i=0; i< 100 ; i++){
            memberRepository.save(new Member("user"+i,i));
        }
    }
}
