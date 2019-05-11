package com.whichard.spring.boot.blog.util;

/**
 * @author wq
 * @date 2019/4/18
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    private static String READ_SIZE = "READ_SIZE";

    public static String getLikeKey(int entityType, long entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, long entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

    public static String getReadSizeKey(long blogId) {
        return READ_SIZE + SPLIT + String.valueOf(blogId);
    }

}
