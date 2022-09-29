package jjfactory.simpleapi.business.balance.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.balance.domain.BalanceHistory;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.seller.domain.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static jjfactory.simpleapi.business.balance.domain.QBalanceHistory.balanceHistory;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class BalanceRepositorySupportTest {
    @Autowired
    EntityManager em;

    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);

        Rider rider = createRiderAndSeller();
        createDeliveriesAndBalanceHistory(rider);
    }

    @Test
    void findBalances(){
        //given
        String startDate = "2022-08-01 00:00:00";
        String endDate = "2022-09-30 23:59:59";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime convertedStart = LocalDateTime.parse(startDate, formatter);
        LocalDateTime convertedEnd = LocalDateTime.parse(endDate, formatter);

        //when
        Integer sum = queryFactory.select(balanceHistory.balance.sum())
                .from(balanceHistory)
                .where(balanceHistory.delivery.appointTime.between(convertedStart, convertedEnd),
                        balanceHistory.delivery.rider.driverId.eq("GG1234"))
                .fetchOne();

        //then
        assertThat(sum).isEqualTo(50000);
    }

    private void createDeliveriesAndBalanceHistory(Rider rider) {
        for(int i = 1; i<21; i++){
            LocalDateTime appointTime = ( i%2 == 0 ) ?  LocalDateTime.now() : LocalDateTime.now().minusMonths(3);

            Delivery delivery = Delivery.builder()
                    .rider(rider)
                    .appointTime(appointTime)
                    .build();

            em.persist(delivery);

            BalanceHistory balanceHistory = BalanceHistory.builder()
                    .balance(5000)
                    .delivery(delivery)
                    .build();
            em.persist(balanceHistory);
        }
    }

    private Rider createRiderAndSeller() {
        Rider rider = Rider.builder()
                .driverId("GG1234")
                .name("tester")
                .password("1234")
                .phone("01012341234")
                .build();

        em.persist(rider);

        Seller findSeller = Seller.builder()
                .name("sellerA")
                .sellerCode("dddd1111wwww")
                .build();
        em.persist(findSeller);
        return rider;
    }

}