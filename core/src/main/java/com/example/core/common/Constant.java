package com.example.core.common;

/**
 *
 * @author: Rainc
 * @date: Created in 2019-07-30 09:03:29
 * @copyright: Copyright (c) 2019
 * @version: V1.0
 * @modified: Rainc
 */
public class Constant {

    /**
     * 管理员标识
     */
    public static final String ADMIN = "Admin";

    /**
     * 用户标识
     */
    public static final String CUSTOMER = "usoneCustomer";

    /**
     * 供应商标识
     */
    public static final String MERCHANT = "merchant";


    /**
     * postman请求token中携带的标识
     */
    public static final String BEARER = "Bearer ";

    /**
     * 用户登陆账号锁头前缀
     */
    public static final String LOGIN_LOCK = "loginLock-";

    /**
     * admin中的userName
     */
    public static final String USERNAME = "userName";

    /**
     * admin中的realName
     */
    public static final String REALNAME = "realName";

    /**
     * redis中权限key标识
     */
    public static final String ACTION = "action";

    /**
     * app、系统间验签时长
     */
    public static final Integer THRESHOLD_SECOND = 30000;

    /**
     * null
     */
    public static final String NULL = "null";

    /**
     * 催收人员角色ID
     */
    public static final Long urgeUserRoleId = 393608273085992960L;

    /**
     * 催收管理员角色ID
     */
    public static final Long urgeAdminRoleId = 392575743742316544L;

    /**
     * 信审管理员角色ID
     */
    public static final Long loanAdminRoleId = 2000L;

    /**
     * 初审人员角色ID
     */
    public static final Long loanProcessingRoleId = 2001L;
    /**
     * 复审人员角色ID
     */
    public static final Long loanUnderWritingRoleId = 2002L;
    /**
     * 超级管理员角色ID
     */
    public static final Long superAdminRoleId = 1L;

    /**
     * notes模板-订单入催收
     */
    public static String  NOTES_LOAN_PAST_DUE= "#%s past due,turn to collection";

    /**
     * 工作流主表 note字段模板（适用于 Review log）
     */
    public static String info = "The order was assigned by %s to %s";

    /**
     * 工作流主表 note字段模板（适用于 Loan status）
     */
    public static String NOTES_LOAN_STATUS = "Loan status changed from %s to %s";

    /**
     * 用户信用额度改变note
     */
    public static final String creditLineBalanceNote = "credit line balance";

    /**
     * 发送邮件note
     */
    public static final String SEND_EMAIL_NOTE = "Sent an email to {0} at the {1} node";

    /**
     * 发送短信note
     */
    public static final String SEND_SMS_NOTE = "Sent a SMS to {0} at the {1} node";

    /**
     * 一天的时间戳
     */
    public static final Long DAY_TIME_STAMP = (60L * 60 * 24 * 1000);

    /**
     * 一周的时间戳
     */
    public static final Long WEEK_TIME_STAMP = DAY_TIME_STAMP * 7;

    /**
     * 30天的时间戳
     */
    public static final Long MONTH_TIME_STAMP = DAY_TIME_STAMP * 30;
    /**
     * 一个小时的秒数
     */
    public static final Long HOUR_TIME_SECOND = (60L * 60);
    /**
     * 一天的秒数
     */
    public static final Long DAY_TIME_SECOND = (60L * 60 * 24);

    /**
     * 进件流程-进件节点ID
     */
    public static final Long NODE_LEAD_RECEIVE = 403096098710818110L;
    /**
     * 进件流程-补充资料节点ID
     */
    public static final Long NODE_ADDITIONAL_DETAILS = 403096098710818120L;
    /**
     * 进件流程-ESign节点ID
     */
    public static final Long NODE_ESIGN = 403096098710818130L;
    /**
     * 进件流程-BankStatement节点ID
     */
    public static final Long NODE_BANK_STATEMENT = 403096098710818140L;
    /**
     * 信审流程-决策引擎节点ID
     */
    public static final Long NODE_DECISION_ENGINE = 403096098710818610L;
    /**
     * redis标识发送邮件定时任务String-进件之后一个小时/一天未签约发送邮件
     */
    public static final String SIGNING_TIMER = "esignIngTimer:";
    /**
     * redis标识发送邮件定时任务String-签约之后一个小时/一天未验证银行信息发送邮件
     */
    public static final String BANK_VERIFYING__Timer = "bankVerifyingTimer:";
    /**
     * redis标识关闭订单定时任务String-进件之后若干天关闭订单
     */
    public static final String SIGNING_CANCEL_TIMER = "esignIngCancelTimer:";
    /**
     * redis标识关闭订单定时任务String-签约之后若干天关闭订单
     */
    public static final String BANK_VERIFYING_CANCEL_Timer = "bankVerifyingCancelTimer:";
    /**
     * 设置验证码+超时
     */
    public static final String VERIFICATION_KEY = "verification:email:";

    /**
     * 调数据系统appId
     * @Author Laity
     */
    public static final Long APP_ID = 412837041988907008L;

    /**
     * 调取数据系统objectId【非LoanId或UserId情况下】
     * @Author Laity
     */
    public static final Long DP_OBJECT_ID = 1L;

    /**
     * 调取数据系统idType【非订单或用户情况下】
     * @Author Laity
     */
    public static final Integer DP_ID_TYPE = 0;

    /**
     * 初审状态
     */
    public static final Integer POCESSING_STATE=1;
    /**
     * 复审状态
     */
    public static final Integer APPROVAL_STATUE=2;

    /**
     * 查询数据系统ServeId【查询破产信息】
     * @Author Laity
     */
    public static final Long QUERY_BANKRUPTCY_LIST_SERVE_ID = 425152075532288000L;

    /**
     * 调取数据系统ServeId【初次绑定pacer账号】
     * @Author Laity
     */
    public static final Long BIND_PACER_ACOUNT_SERVE_ID = 425152318583816192L;

    /**
     * 调取数据系统ServeId【修改Pacer密码或更换Pacer账号】
     * @Author Laity
     */
    public static final Long CHANGE_PACER_ACCOUNT_SERVE_ID = 425152451597778944L;
}
