package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    void 회원가입() {
        Member member = new Member();
        member.setName("kim");

        Long savedId = memberService.join(member);
        Member findMember = memberRepository.findOne(savedId);
        em.flush();

        assertThat(member).isEqualTo(findMember);
    }

    @Test
    void 중복_회원_예외() {
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        memberService.join(member1);
        Assert.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
    }

    @Test
    void findMembers() {
    }

    @Test
    void findMember() {
    }
}