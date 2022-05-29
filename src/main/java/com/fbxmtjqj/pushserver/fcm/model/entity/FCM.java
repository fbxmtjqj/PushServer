package com.fbxmtjqj.pushserver.fcm.model.entity;

import com.fbxmtjqj.pushserver.common.model.entity.EntityDate;
import com.fbxmtjqj.pushserver.user.model.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FCM extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Setter
    @ManyToOne
    private User user;
}
