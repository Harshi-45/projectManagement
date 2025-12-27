package com.projectManagement.project.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity {

    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;

    @Column(updatable = false)
    protected Instant createdAt;

    @LastModifiedBy
    protected String modifiedBy;

    protected Instant modifiedAt;

    // Soft delete flag
    protected Boolean deleted = false;

    @PrePersist
    public void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.modifiedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.modifiedAt = Instant.now();
    }
}
