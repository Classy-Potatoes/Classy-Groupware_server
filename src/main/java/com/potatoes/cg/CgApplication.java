package com.potatoes.cg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 이 어노테이션이 전체적인것에 달려있어야 한다.
// JPA에서 @CreatedDate, @LastModifiedDate 관련 시간 기록
@EnableJpaAuditing
@SpringBootApplication
public class CgApplication {

    public static void main(String[] args) {
        SpringApplication.run(CgApplication.class, args);
    }

}
