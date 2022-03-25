package com.example.VivaLaTrip.Entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass//jpa가 클래스를 상속할 경우 필드들도 칼럼으로 인식
@EntityListeners(AuditingEntityListener.class)//클래스에 Auditing 기능(jpa에서 시간테이블 자동으로 넣음) 포함
public class BaseTimeEntity {
    @CreatedDate//엔티티저장될 때 자동으로 시간도 저장
    private LocalDateTime createdDate;

    @LastModifiedDate//엔티티 변경될 때 자동으로 시간도 저장
    private LocalDateTime modifiedDate;
}
