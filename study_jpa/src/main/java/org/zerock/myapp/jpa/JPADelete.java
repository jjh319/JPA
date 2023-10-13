package org.zerock.myapp.jpa;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.zerock.myapp.domain.Board;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.Objects;

@Log4j2
public class JPADelete {

    public static void main(String ... args) {
        log.trace("main({}) invoked.", Arrays.toString(args));

        @Cleanup
        EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("H2");

        @Cleanup
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();

        try {
            tx.begin();

            // 삭제할 Board 엔티티 생성 xx
//            Board board = new Board();
//            board.setSeq(50L);

            // 검색을 통해 찾아낸 엔티티사용
            Board board = entityManager.find(Board.class,20L);

            Objects.requireNonNull(board);
            entityManager.remove(board);

            tx.commit();
        } catch(Exception e) {
            tx.rollback();

            e.printStackTrace();
        } // try-catch

    } // main

} // end class
