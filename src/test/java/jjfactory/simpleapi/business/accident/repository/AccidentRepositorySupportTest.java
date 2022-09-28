package jjfactory.simpleapi.business.accident.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jjfactory.simpleapi.business.accident.domain.Accident;
import jjfactory.simpleapi.business.delivery.domain.Delivery;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.global.dto.req.MyPageReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static jjfactory.simpleapi.business.accident.domain.QAccident.accident;
import static jjfactory.simpleapi.business.delivery.domain.QDelivery.delivery;
import static jjfactory.simpleapi.business.rider.domain.QRider.rider;
import static jjfactory.simpleapi.business.seller.domain.QSeller.seller;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class AccidentRepositorySupportTest {
    @Autowired
    private EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);

        Rider rider = createRiderAndSeller();
        createDeliveriesAndAccidents(rider);
    }



    @Test
    @DisplayName("라이더 개인 사고 조회 성공")
    void findAccidentsByPhone() {
        //given
        PageRequest pageReq = new MyPageReq(1, 10).of();

        //when
        List<Accident> accidents = queryFactory.selectFrom(accident)
                .where(accident.delivery.rider.phone.eq("01012341234"))
                .orderBy(accident.accidentTime.desc())
                .offset(pageReq.getOffset())
                .limit(pageReq.getPageSize())
                .fetch();

        int total = queryFactory.selectFrom(accident)
                .where(accident.delivery.rider.phone.eq("01012341234"))
                .orderBy(accident.accidentTime.desc())
                .fetch().size();

        //then
        assertThat(total).isEqualTo(15);
        assertThat(accidents.size()).isEqualTo(10);
        assertThat(accidents.get(0).getAccidentTime())
                .isAfter(accidents.get(9).getAccidentTime());
    }

    @Test
    @DisplayName("운영사 총 보상금 조회")
    void findTotalCompensation() {
        //when
        Integer totalCompensation = queryFactory.select(accident.compensation.sum())
                .from(accident)
                .innerJoin(delivery)
                .on(accident.delivery.id.eq(delivery.id))
                .innerJoin(rider)
                .on(delivery.rider.id.eq(rider.id))
                .innerJoin(seller)
                .on(seller.sellerCode.eq("dddd1111wwww"))
                .where(seller.sellerCode.eq("dddd1111wwww"))
                .fetchOne();

        assertThat(totalCompensation).isEqualTo(15000);
    }

    private void createDeliveriesAndAccidents(Rider rider) {
        for(int i = 1; i<16; i++){
            Delivery delivery = Delivery.builder()
                    .rider(rider)
                    .build();

            em.persist(delivery);

            Accident findAccident = Accident.builder()
                    .delivery(delivery)
                    .compensation(1000)
                    .accidentTime(LocalDateTime.now())
                    .build();

            em.persist(findAccident);
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