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
public class JPASelect {

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


            Board foundBoard = entityManager.find(Board.class,58L);
            log.info("\t foundBoard: {}", foundBoard);

            tx.commit();
        } catch(Exception e) {
            tx.rollback();

            e.printStackTrace();
        } // try-catch

    } // main

} // end class
