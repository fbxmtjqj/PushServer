package com.fbxmtjqj.pushserver.common.model.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class EntityDate {
    @CreatedDate
    @Column(nullable = false, length = 20, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(length = 20)
    private LocalDateTime modifiedDate;
}
