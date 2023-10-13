package org.zerock.myapp.jpa;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.zerock.myapp.domain.Board;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;

@Log4j2
public class JPAInsert {

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

            Board board = new Board();
            board.setTitle("NEW_TITLE");
            board.setWriter("Pyramide");
            board.setContent("NEW_CONTENT");
            log.info("\t Before persist: {}", board);

            // 영구적으로 테이블에 저장
            entityManager.persist(board);

            log.info("\t After persist: {}", board);

            tx.commit();
        } catch(Exception e) {
            tx.rollback();

            e.printStackTrace();
        } // try-catch

    } // main

} // end class
