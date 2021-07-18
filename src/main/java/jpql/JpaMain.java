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

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(0);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(0);
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(0);
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();
            //영속성 컨텍스트의 member1,2,3의 나이는 모두 0

            //1. FLUSH(DB반영) : DB에 영속성 데이터(나이 = 0) 넣는다! 영속성 컨텍스트엔는 여전히 나이=0으로 남아잇음.
            //2. 아래 벌크 연산으로 나이는 20으로 업데이트.
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();
            System.out.println("resultCount = " + resultCount);//DB에서 member1,2,3의 나이는 모두 20

            em.clear();//DB에서 데이터 가져오도록 영속성 컨텍스트 초기화

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember.getAge() = " + findMember.getAge());

            //아래에서 member 나이 출력해보면 모두 0(영속성 컨텍스트)으로 나온는 것을 알 수 있음.
            //System.out.println("member1.getAge() = " + member1.getAge());
            //System.out.println("member2.getAge() = " + member2.getAge());
            //System.out.println("member3.getAge() = " + member3.getAge());

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
