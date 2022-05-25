package com.fbxmtjqj.pushserver.user.model.entity;

import com.fbxmtjqj.pushserver.common.model.entity.EntityDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "groups")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;
}
