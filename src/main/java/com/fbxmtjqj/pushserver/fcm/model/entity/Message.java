package com.fbxmtjqj.pushserver.fcm.model.entity;

import com.fbxmtjqj.pushserver.common.model.entity.EntityDate;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int successCount;

    @Column(nullable = false)
    private int failCount;
}
