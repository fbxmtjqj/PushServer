package com.fbxmtjqj.pushserver.fcm.model.repository;

import com.fbxmtjqj.pushserver.fcm.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
