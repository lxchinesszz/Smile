package org.smileframework.tool.config;

import lombok.Data;

/**
 * @Description: Boluome配置文件
 * @author: liuxin
 * @date: 2017/7/20 下午2:17
 */
@Data
public abstract class BlmAbsConfig {

    /**
     * 合作商APPCODE
     */
    private String appCode;
    /**
     * 订单详情基础地址
     */
    private String orderDetailUrl;
    /**
     * 内部通知地址
     */
    private String paySuccUrl;
    /**
     * 支付成功通知地址
     */
    private String payNotifyUrl;
    /**
     * 修改订单状态接口
     */
    private String status;
    /**
     * 直接退款地址
     * 该方法调用后，直接，退款成功
     */
    private String refundUrl;
    /**
     * session服务操作mongo地址
     */
    private String sessionMongoUrl;
    /**
     * 查询地址
     * ${SESSION_SERVER_ADDRESS}/sm/order/fetch/${id
     */
    private String fetchUrl;
    /**
     * 更新地址
     * ${SESSION_SERVER_ADDRESS}/sm/order/update
     */
    private String updateUrl;
}
