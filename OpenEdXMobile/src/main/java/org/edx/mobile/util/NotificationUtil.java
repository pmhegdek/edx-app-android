package org.edx.mobile.util;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;

import org.edx.mobile.logger.Logger;
import org.edx.mobile.notifications.services.NotificationService;

public class NotificationUtil {
    final static Logger logger = new Logger(NotificationUtil.class.getName());

    /**
     * Subscribe to the Topic channels that will be used to send Group notifications
     */
    public static void subscribeToTopics(Config config) {
        if (config.areFirebasePushNotificationsEnabled()) {
            FirebaseMessaging.getInstance().subscribeToTopic(
                NotificationService.NOTIFICATION_TOPIC_RELEASE
            );
        }
    }

    /**
     * UnSubscribe from all the Topic channels
     */
    public static void unsubscribeFromTopics(Config config) {
        if (config.getFirebaseConfig().isEnabled()) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(
                NotificationService.NOTIFICATION_TOPIC_RELEASE
            );
        }
    }
}
