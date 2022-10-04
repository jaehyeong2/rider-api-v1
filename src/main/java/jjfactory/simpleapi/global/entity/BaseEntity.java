package jjfactory.simpleapi.global.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public BaseEntity(LocalDateTime createDate, LocalDateTime modifiedDate, DeleteStatus deleteStatus, LocalDateTime deleteDate) {
        super(createDate, modifiedDate);
        this.deleteStatus = deleteStatus;
        this.deleteDate = deleteDate;
    }
}
