package jjfactory.simpleapi.domain.rider.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RiderQueryRepository {
    private final JPAQueryFactory queryFactory;
}
