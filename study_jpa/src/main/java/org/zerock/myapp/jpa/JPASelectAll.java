package org.zerock.myapp.jpa;


import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.zerock.myapp.domain.Board;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Log4j2
public class JPASelectAll {
    private static final String persistenceUnitName = "H2";

    public static void main(String ... args) {
        log.trace("main({}) invoked.", args);

        @Cleanup
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory(persistenceUnitName);

        @Cleanup
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        // JPQL 문장으로 수행
        TypedQuery<Board> typedQuery = entityManager.createQuery("FROM Board", Board.class);
        List<Board> allBoards = typedQuery.getResultList();

        Objects.requireNonNull(allBoards);
        allBoards.forEach(log::info);

        //=========================================
        entityManager.getTransaction().begin();

        allBoards.forEach(board -> {
            board.setCnt(board.getCnt() + 1);
        });

        entityManager.getTransaction().commit();

    } // main

} // end class
