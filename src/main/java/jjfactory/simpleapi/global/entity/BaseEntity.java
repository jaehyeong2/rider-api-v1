package jjfactory.simpleapi.global.entity;


import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity{
    private DeleteStatus deleteStatus = DeleteStatus.N;
    private LocalDateTime deleteDate;

    public void delete() {
        this.deleteStatus = DeleteStatus.Y;
        this.deleteDate = LocalDateTime.now();
    }
}
