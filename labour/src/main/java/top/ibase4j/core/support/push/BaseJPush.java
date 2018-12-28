package top.ibase4j.core.support.push;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import top.ibase4j.core.util.ExceptionUtil;

public abstract class BaseJPush {
    protected Logger logger = LogManager.getLogger();

    public abstract JPushClient getJPushClient();

    /**
     * 给ios设备推送一条消息的
     */
    public boolean sendNotificationIOS(String title, String alert, Map<String, String> extras,
        String... registrationId) {
        Audience audience;
        if (registrationId != null && registrationId.length > 0) {
            audience = Audience.registrationId(registrationId);
        } else {
            audience = Audience.all();
        }

        IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody(title, "", alert).build();
        PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(audience)
                .setNotification(Notification.newBuilder()
                .addPlatformNotification(IosNotification.newBuilder().setAlert(iosAlert).addExtras(extras)
                    .setSound(extras.get("sound")).build())
                    .build())
                .setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
        try {
            PushResult result = getJPushClient().sendPush(payload);
            logger.info("==JPUSH==>{}", JSON.toJSONString(result));
            return true;
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTraceAsString(e));
        }
        return false;
    }

    /**
     * 安卓和ios设备推送一条消息的
     */
    public boolean sendNotificationAndroid(String title, String alert, Map<String, String> extras,
        String... registrationId) {
        Audience audience;
        if (registrationId != null && registrationId.length > 0) {
            audience = Audience.registrationId(registrationId);
        } else {
            audience = Audience.all();
        }

        PushPayload payload = PushPayload.newBuilder().setAudience(audience).setPlatform(Platform.all())
                .setNotification(Notification.newBuilder()
                    .addPlatformNotification(
                        AndroidNotification.newBuilder().setTitle(title).setAlert(alert).addExtras(extras).build())
                    .build())
                .setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
        try {
            PushResult result = getJPushClient().sendPush(payload);
            logger.info("==JPUSH==>{}", JSON.toJSONString(result));
            return true;
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTraceAsString(e));
        }
        return false;
    }

    /**
     * 安卓和ios设备推送一条消息的
     */
    public boolean sendNotificationAll(String title, String alert, Map<String, String> extras,
        String... registrationId) {
        Audience audience;
        if (registrationId != null && registrationId.length > 0) {
            audience = Audience.registrationId(registrationId);
        } else {
            audience = Audience.all();
        }

        IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody(title, "", alert).build();
        PushPayload payload = PushPayload.newBuilder().setAudience(audience).setPlatform(Platform.all())
                .setNotification(Notification.newBuilder()
                    .addPlatformNotification(IosNotification.newBuilder().setAlert(iosAlert).addExtras(extras)
                        .setSound(extras.get("sound")).build())
                    .addPlatformNotification(
                        AndroidNotification.newBuilder().setTitle(title).setAlert(alert).addExtras(extras).build())
                    .build())
                .setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
        try {
            PushResult result = getJPushClient().sendPush(payload);
            logger.info("==JPUSH==>{}", JSON.toJSONString(result));
            return true;
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTraceAsString(e));
        }
        return false;
    }
}
