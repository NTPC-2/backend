package Insuleng.Insuleng_Backend.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

    //영속성 컨텐스트에 동시적으로 접근하면 문제가 생기기에(데이터 정합성 부분)
    //동시성 문제를 해결하기 위해 @PersistenceContext를 사용
    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }

}
