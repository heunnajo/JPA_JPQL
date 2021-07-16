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
            //Member에 Team을 저장하기 위해 Team 먼저.
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(20);
            member.setTeam(team);

            em.persist(member);

            em.flush();//DB에 반영
            em.clear();//영속성 컨텍스트 비운다.

            String query = "select mm.age, mm.username " +
                                "from (select m.age, m.username from Member m) as mm";

            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();
            System.out.println("result.siz = " + result.size());
//                    .setFirstResult(1)
//                    .setMaxResults(10)

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
