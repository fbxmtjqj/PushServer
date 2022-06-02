package com.fbxmtjqj.pushserver.common.dto;

import com.fbxmtjqj.pushserver.fcm.model.entity.FCM;

public class FCMDTO {
    public static FCM getFCM() {
        return getFCM("fcmToken");
    }

    public static FCM getFCM(String fcmToken) {
        return FCM.builder().token(fcmToken).build();
    }
}
