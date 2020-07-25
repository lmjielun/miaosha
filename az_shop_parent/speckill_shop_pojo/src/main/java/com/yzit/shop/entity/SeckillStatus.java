package com.yzit.shop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *  排队对象
 */
public class SeckillStatus implements Serializable {
    private Integer userId;
    //创建时间
    private Date createTime;
    //秒杀状态  1:排队中，2:秒杀等待支付,3:支付超时，4:秒杀失败,5:支付完成
    private Integer status;
    //秒杀的商品ID
    private Integer goodsId;
    //应付金额
    private Integer money;
    //订单号
    private Long orderId;

    public SeckillStatus() {
    }

    public SeckillStatus(Integer userId, Date createTime, Integer goodsId) {
        this.userId = userId;
        this.createTime = createTime;
        this.goodsId = goodsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
