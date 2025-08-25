package ssu.cromi.teamit.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 이 클래스가 Spring의 설정 파일임을 나타냄
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean // 이 메서드가 반환하는 객체(JPAQueryFactory)를 Spring Bean으로 등록
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}