package com.community.common;

/**
 * 常量定义
 */
public class Constants {

    // 用户角色
    public static final String ROLE_RESIDENT = "resident";      // 居民
    public static final String ROLE_VOLUNTEER = "volunteer";    // 志愿者
    public static final String ROLE_ADMIN = "admin";            // 管理员

    // 用户状态
    public static final Integer USER_STATUS_DISABLED = 0;       // 禁用
    public static final Integer USER_STATUS_NORMAL = 1;         // 正常

    // 工单状态
    public static final Integer PETITION_STATUS_SUBMITTED = 0;     // 已提交
    public static final Integer PETITION_STATUS_ASSIGNED = 1;      // 已分派
    public static final Integer PETITION_STATUS_PROCESSING = 2;    // 处理中
    public static final Integer PETITION_STATUS_RESOLVED = 3;      // 已办结
    public static final Integer PETITION_STATUS_ARCHIVED = 4;      // 已归档

    // 活动状态
    public static final Integer ACTIVITY_STATUS_NOT_STARTED = 0;   // 未开始
    public static final Integer ACTIVITY_STATUS_IN_PROGRESS = 1;   // 进行中
    public static final Integer ACTIVITY_STATUS_ENDED = 2;         // 已结束

    // 订单状态
    public static final Integer ORDER_STATUS_PENDING = 0;      // 待支付
    public static final Integer ORDER_STATUS_PAID = 1;         // 已支付
    public static final Integer ORDER_STATUS_COMPLETED = 2;    // 已完成
    public static final Integer ORDER_STATUS_CANCELLED = 3;    // 已取消

    // 积分类型
    public static final String POINTS_TYPE_EARN = "earn";          // 获得
    public static final String POINTS_TYPE_SPEND = "spend";        // 消费
    public static final String POINTS_TYPE_EXCHANGE = "exchange";  // 兑换
    public static final String POINTS_TYPE_REFUND = "refund";      // 退款

    // 积分来源
    public static final String POINTS_SOURCE_ACTIVITY = "activity";           // 活动
    public static final String POINTS_SOURCE_ORDER = "order";                 // 订单
    public static final String POINTS_SOURCE_REDEEM = "redeem";               // 兑换
    public static final String POINTS_SOURCE_ADMIN = "admin";                 // 管理员发放

    // 政策分类
    public static final Integer POLICY_CATEGORY_POLICY = 1;      // 政策
    public static final Integer POLICY_CATEGORY_NOTICE = 2;      // 通知
    public static final Integer POLICY_CATEGORY_ANNOUNCE = 3;    // 公告

    // Redis Key前缀
    public static final String REDIS_USER_TOKEN = "user:token:";
    public static final String REDIS_USER_INFO = "user:info:";
    public static final String REDIS_POINTS_DAILY = "points:daily:";
    public static final String REDIS_SMS_CODE = "sms:code:";

    // 积分规则
    public static final Integer POINTS_ACTIVITY_SIGNUP = 50;        // 活动报名
    public static final Integer POINTS_ACTIVITY_COMPLETE = 100;     // 活动完成
    public static final Integer POINTS_DAILY_CHECKIN = 10;          // 每日签到
    public static final Integer POINTS_PETITION_SUBMIT = 5;         // 提交工单

    // 积分兑换比例
    public static final Integer POINTS_EXCHANGE_RATE = 100;         // 100积分=1元

    // 分页默认值
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAX_PAGE_SIZE = 50;
}
