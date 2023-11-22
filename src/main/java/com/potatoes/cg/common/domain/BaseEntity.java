//package com.potatoes.cg.common.domain;
//
//import com.ohgiraffers.comprehensive.common.domain.type.StatusType;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import javax.persistence.Column;
//import javax.persistence.EntityListeners;
//import javax.persistence.Enumerated;
//import javax.persistence.MappedSuperclass;
//import java.time.LocalDateTime;
//
//import static com.ohgiraffers.comprehensive.common.domain.type.StatusType.USABLE;
//import static javax.persistence.EnumType.STRING;
//
//@Getter
//// 상위타입으로 해서 사용할거면 이게 꼭 필요, 엔티티 대표
//@MappedSuperclass
//@NoArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
//public abstract class BaseEntity {
//    /* 자주 쓰는 엔티티 타입이 있을경우 미리 abstract 으로 선언해놓고
//    * 상위 타입으로 만들어서 호출해서 사용하면 편하다.  */
//
//    @CreatedDate
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(nullable = false)
//    private LocalDateTime modifiedAt;
//
//    @Column(nullable = false)
//    @Enumerated(value = STRING)
//    private StatusType status = USABLE;
//
//}