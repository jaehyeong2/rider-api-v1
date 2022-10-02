package jjfactory.simpleapi.business.delivery.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.delivery.domain.BalanceHistory;
import jjfactory.simpleapi.business.delivery.domain.Address;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.delivery.dto.DeliveryRes;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static jjfactory.simpleapi.business.delivery.domain.QBalanceHistory.balanceHistory;
import static jjfactory.simpleapi.business.delivery.domain.QDelivery.delivery;
import static org.assertj.core.api.Assertions.*;


@DataJpaTest
class DeliveryRepositorySupportTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;
    Long idTmp = 0L;

    @BeforeEach
    public void init(TestInfo info) {
        queryFactory = new JPAQueryFactory(em);
        Rider rider = createRiderAndSeller();
        idTmp = rider.getId();

        if(info.getDisplayName().equals("하루 전체 운행 아력조회 성공")){
            createDeliveriesAndBalanceHistory2(rider);
        }else{
            createDeliveriesAndBalanceHistory(rider);
        }
    }

    @Test
    @DisplayName("하루 전체 운행 아력조회 성공")
    void findTotalDeliveriesToday() {
        //when
        List<DeliveryRes> deliveries = queryFactory.select(Projections.constructor(DeliveryRes.class, delivery, balanceHistory))
                .from(delivery)
                .join(balanceHistory)
                .on(delivery.eq(balanceHistory.delivery))
                .where(betweenToday())
                .orderBy(delivery.appointTime.desc())
                .fetch();

        int sum = deliveries.stream().mapToInt(DeliveryRes::getBalance).sum();

        //then
        assertThat(deliveries.size()).isEqualTo(8);
        assertThat(sum).isEqualTo(800);
    }

    @Test
    @DisplayName("라이더 운행 아력조회 성공")
    void findMyDeliveries() {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime convertedStart = LocalDateTime.parse("2022-08-01 00:00:00", formatter);
        LocalDateTime convertedEnd = LocalDateTime.parse("2022-09-30 23:59:59", formatter);

        PageRequest req = new MyPageReq(1, 10).of();

        //when
        List<DeliveryRes> deliveries = queryFactory.select(Projections.constructor(DeliveryRes.class, delivery))
                .from(delivery)
                .where(delivery.appointTime.between(convertedStart, convertedEnd),
                        delivery.id.isNotNull(),
                        delivery.rider.id.eq(idTmp))
                .offset(req.getOffset())
                .limit(req.getPageSize())
                .orderBy(delivery.appointTime.desc())
                .fetch();

        int count = queryFactory.select(Projections.constructor(DeliveryRes.class, delivery))
                .from(delivery)
                .where(delivery.appointTime.between(convertedStart, convertedEnd),
                        delivery.id.isNotNull(),
                        delivery.rider.id.eq(idTmp))
                .fetch().size();

        //then
        assertThat(count).isEqualTo(7);
        assertThat(LocalDateTime.parse(deliveries.get(0).getAppointTime(),formatter))
                .isAfter(LocalDateTime.parse(deliveries.get(6).getAppointTime(),formatter));
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

    private void createDeliveriesAndBalanceHistory(Rider rider) {
        for(int i = 1; i<16; i++){
            LocalDateTime appointTime = ( i%2 == 0 ) ?
                    LocalDateTime.of(2022,8,1,0,0,0)
                    : LocalDateTime.now().minusMonths(3);

            Delivery delivery = Delivery.builder()
                    .rider(rider)
                    .appointTime(appointTime.plusMinutes(i))
                    .address(new Address("주소1","주소2","배달주소1","배달주소2"))
                    .build();

            BalanceHistory balanceHistory = BalanceHistory.create(100, delivery);

            em.persist(delivery);
            em.persist(balanceHistory);
        }
    }

    private void createDeliveriesAndBalanceHistory2(Rider rider) {
        for(int i = 1; i<16; i++){
            LocalDateTime appointTime = ( i%2 == 0 ) ?
                    LocalDateTime.now()
                    : LocalDateTime.now().minusDays(1);

            Delivery delivery = Delivery.builder()
                    .rider(rider)
                    .appointTime(appointTime.plusMinutes(i))
                    .address(new Address("주소1","주소2","배달주소1","배달주소2"))
                    .build();

            BalanceHistory balanceHistory = BalanceHistory.create(100, delivery);

            em.persist(delivery);
            em.persist(balanceHistory);
        }
    }

    private BooleanExpression betweenToday() {
        return delivery.appointTime.between(
                LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(6,0,0))
                , LocalDateTime.of(LocalDate.now(), LocalTime.of(5,59,59)));
    }

}