package org.zerock.myapp.domain;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

// JPA의 Entity 요건에 맞게, 표준 자바빈 규약 대로 클래스 선언
@Data

@Entity//(name = "wlgns")  // Default: Entity Name == Entity Class Name.
@Table(name = "board")
public class Board {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO) // Default Strategy.
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // OCI : Identity Column
//    @GeneratedValue(strategy = GenerationType.SEQUENCE) // 시퀀스객체 생성해서, 키값을 넣으라
//    @GeneratedValue(strategy = GenerationType.TABLE) // 인조키 생선전용 테이블이용해서 값 생성
    @GeneratedValue
    private Long seq;

    private String title;
    private String writer;
    private String content;
    private Long cnt = 0L;

    private String new_columns;

    // 정보통신망법에 따라 필요한 필드
    @CreationTimestamp
    private Date insert_ts;     // 최초등록일시
    @UpdateTimestamp
    private Date update_ts;     // 최종수정일시

} // end class
