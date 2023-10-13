package org.zerock.myapp.jpa;


import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.jupiter.api.*;
import org.zerock.myapp.domain.Board;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import java.io.Serializable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@NoArgsConstructor

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JPABootStrapProcessTests {
    // Defined in the `classpath:/META-INF/persistence.xml` JPA configuration file.
    private static final String persistenceUnitName = "H2";

    
    @Test
    @Order(1)
    @DisplayName("1. testGetEntityManagerFactory")
    void testGetEntityManagerFactory() {
        log.trace("testGetEntityManagerFactory() invoked.");

        @Cleanup
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory(persistenceUnitName);

        log.info("\t entityManagerFactory: {}", entityManagerFactory);

    } // testGetEntityManagerFactory


    @Test
    @Order(2)
    @DisplayName("2. testGetSessionFactory")
    void testGetSessionFactory() {
        log.trace("testGetSessionFactory() invoked.");

        // sessionFactory 객체 생성
        // Auto-closeable 이므로 반드시 close 해줘야 합니다.
        @Cleanup
        SessionFactory sessionFactory =
                (SessionFactory) Persistence.createEntityManagerFactory(persistenceUnitName);

        log.info("\t sessionFactory: {}", sessionFactory);

    } // testGetSessionFactory


    @Test
    @Order(3)
    @DisplayName("2. testGetSession")
    void testGetSession() {
        log.trace("testGetSession() invoked.");

        @Cleanup
        SessionFactory sessionFactory =
                (SessionFactory) Persistence.createEntityManagerFactory(persistenceUnitName);

        assertNotNull(sessionFactory);
        Session session = sessionFactory.openSession();
        log.info("\t session: {}", session);

    } // testGetSession


    @Test
    @Order(4)
    @DisplayName("2. testGetTransaction")
    void testGetTransaction() {
        log.trace("testGetTransaction() invoked.");

        @Cleanup
        SessionFactory sessionFactory =
                (SessionFactory) Persistence.createEntityManagerFactory(persistenceUnitName);

        assertNotNull(sessionFactory);

//        @Cleanup
        Session session = sessionFactory.openSession();
        log.info("\t session: {}", session);

        Transaction tx = session.getTransaction();
        assertNotNull(tx);
        log.info("\t tx: {}",tx);

        try(session) {
            tx.begin();

            Board board = new Board();
            board.setTitle("NEW_TITLE");
            board.setWriter("wlgns");
            board.setContent("NEW_CONTENT");
            log.info("\t board before saving: {}", board);

            session.save(board);

            log.info("\t board after saving: {}", board);

            tx.commit();
        } catch(RuntimeException e) {
            tx.rollback();
            throw e;
        } // try-with-resources

    } // testGetTransaction


    @Test
    @Order(5)
    @DisplayName("4. testSaveEntity")
    void testSaveEntity() {
        log.trace("testSaveEntity() invoked.");

        @Cleanup
        SessionFactory sessionFactory =
                (SessionFactory) Persistence.createEntityManagerFactory(persistenceUnitName);

        assertNotNull(sessionFactory);

//        @Cleanup
        Session session = sessionFactory.openSession();
        log.info("\t session: {}", session);

        Transaction tx = session.getTransaction();
        assertNotNull(tx);
        log.info("\t tx: {}",tx);

        try(session) {
            tx.begin();

            for(int i=1; i<3; i++) {
                Board board = new Board();
                board.setTitle("NEW_TITLE");
                board.setWriter("wlgns");
                board.setContent("NEW_CONTENT");
                log.info("\t board before saving: {}", board);

                session.save(board);
                log.info("\t board after saving: {}", board);
            } // for

            tx.commit();
        } catch(RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            TransactionStatus txStatus = tx.getStatus();
            boolean isOK = txStatus.isOneOf(TransactionStatus.COMMITTED,TransactionStatus.ROLLED_BACK);
            log.info("\t isOK: {}, status: {}", isOK, txStatus);
        } // try-catch-finally

    } // testSaveEntity


    @Test
    @Order(6)
    @DisplayName("5. testFindEntity")
    void testFindEntity() {
        log.trace("testFindEntity() invoked.");

        @Cleanup
        SessionFactory sessionFactory =
                (SessionFactory) Persistence.createEntityManagerFactory(persistenceUnitName);

        Session session = sessionFactory.openSession();

        try(session) {
            Transaction tx = session.getTransaction();

            try {
                tx.begin();

                Board foundBoard = session.<Board>find(Board.class, 11L);
                assert foundBoard != null;
                log.info("\t foundBoard: {}", foundBoard);

                tx.commit();
            } catch(Exception e) {
                tx.rollback();

                throw e;
            } // inner-try-catch

        }

    } // testFindEntity


    @Test
    @Order(7)
    @DisplayName("6. testUpdateEntity")
    void testUpdateEntity() {
        log.trace("testUpdateEntity() invoked.");

        @Cleanup
        SessionFactory sessionFactory =
                (SessionFactory) Persistence.createEntityManagerFactory(persistenceUnitName);

        @Cleanup
        Session session = sessionFactory.openSession();

        Transaction tx = session.getTransaction();

        try {
            tx.begin();

            Board foundBoard = session.find(Board.class, 33L);
            log.info("\t foundBoard: {}", foundBoard);

            assertNotNull(foundBoard);
            foundBoard.setCnt( foundBoard.getCnt() + 1 );

//            session.save(foundBoard);   // 영속화(Persist) --> INSERT (XX)
//            session.saveOrUpdate(foundBoard);
//            session.update(foundBoard);
            session.update("board", foundBoard);
            session.saveOrUpdate("board", foundBoard);

            tx.commit();
        } catch(Exception e) {
            tx.rollback();

            throw e;
        } // try-catch

    } // testUpdateEntity


    @Test
    @Order(8)
    @DisplayName("6. testDeleteEntity")
    void testDeleteEntity() {
        log.trace("testDeleteEntity() invoked.");

        @Cleanup
        SessionFactory sessionFactory =
                (SessionFactory) Persistence.createEntityManagerFactory(persistenceUnitName);

        @Cleanup
        Session session = sessionFactory.openSession();

        Transaction tx = session.getTransaction();

        try {
            tx.begin();

            Board foundBoard = session.find(Board.class, 30L);
            log.info("\t foundBoard: {}", foundBoard);

            session.delete(foundBoard);
//            session.delete("board",foundBoard);

//            Board boardTobeDeleted = new Board();
//            boardTobeDeleted.setSeq(30L);
//
//            session.delete(boardTobeDeleted);    // xx

            tx.commit();
        } catch(Exception e) {
            tx.rollback();

            throw e;
        } // try-catch

    } // testDeleteEntity


    @Test
    @Order(9)
    @DisplayName("8. testFindAllEntities")
    void testFindAllEntities() {
        log.trace("testFindAllEntities() invoked.");

        @Cleanup
        SessionFactory sessionFactory =
                (SessionFactory) Persistence.createEntityManagerFactory(persistenceUnitName);

        @Cleanup
        Session session = sessionFactory.openSession();

        Transaction tx = session.getTransaction();


//        Query query = session.createQuery("SELECT seq, title, content,writer,cnt FROM Board b");
//        List list = query.getResultList();

        NativeQuery<Board> nativeQuery = session.createNativeQuery("SELECT * FROM board", Board.class);
        List<Board> list = nativeQuery.getResultList();

        assertNotNull(list);
        list.forEach(log::info);


    } // testFindAllEntities

} // end class
