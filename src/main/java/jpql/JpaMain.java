package jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//설정파일에서 설정한 이름 가져온다

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {//m은 멤버 자체를 가리킨다!
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(21);
            em.persist(member);

            em.flush();//DB에 반영
            em.clear();//영속성 컨텍스트 비운다.

            List<MemberDTO> result = em.createQuery("select distinct new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();//현재 타입이 없음.

            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO.username = " + memberDTO.getUsername());
            System.out.println("memberDTO.age = " + memberDTO.getAge());

//            List<Team> teamResult = em.createQuery("select t from Member m join m.team t", Team.class).getResultList();
//            em.createQuery("select o.address from Order o", Address.class).getResultList();
//            em.createQuery("select distinct m.username, m.age from Member B");

//            List<Member> memberResult = em.createQuery("select m from Member m", Member.class).getResultList();
//            List<Team> teamResult = em.createQuery("select m.team from Member m", Team.class).getResultList();
//            Member findMember = memberResult.get(0);
//            findMember.setAge(20);//값이 바뀌면 영속성 컨텍스트에서 관리되는 것. 안 바뀌면 관리되지 않는 것.
            //영속성 컨텍스트가 관리하면 현재 영속성 컨텍스트가 비어있기 때문에 DB로 조회한다.


            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();//em이 결국 DB 연결을 담당하기 때문에 자원을 다 쓰고 작업 끝나면 닫아줘야한다!!
        }
        emf.close();//애플리케이션 종료 또는 WAS 내려갈 때 emf도 닫아준다!
    }
}
