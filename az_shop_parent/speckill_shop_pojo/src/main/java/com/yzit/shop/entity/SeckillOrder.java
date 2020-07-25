package com.yzit.shop.entity;
import com.yzit.framework.web.ui.BaseEntity;

import java.util.Date;

/**
 * 
 * <br>
 * <b>功能：</b>秒杀订单实体类<br>
 * <b>作者：</b>Administrator<br>
 * <b>日期：</b> 2020-06-12 <br>
 * <b>版权所有： 2020，云优众<br>
 */ 
public class SeckillOrder extends BaseEntity {
   private static final long serialVersionUID = 1L;
    private String  id;//主键
    private Integer  seckillId;//秒杀商品ID
    private Integer  money;//支付金额
    private Integer  userId;//用户
    private java.util.Date  createTime;//创建时间
    private java.util.Date  payTime;//支付时间
    private String  status;//状态 0未支付，1 已支付
    private String  receiverAddress;//收货人地址
    private String  receiverMobile;//收货人电话
    private String  receiver;//收货人
    private String  transactionId;//交易流水


	public SeckillOrder() {
	}

	public SeckillOrder(String id, Integer seckillId, Integer money, Integer userId, Date createTime, String status) {
		this.id = id;
		this.seckillId = seckillId;
		this.money = money;
		this.userId = userId;
		this.createTime = createTime;
		this.status = status;
	}

	public String getId() {
		return id;
	}
	public void setId(String  id) {
		this.id = id;
	}
	
   	public Integer getSeckillId() {
		return seckillId;
	}
	public void setSeckillId(Integer  seckillId) {
		this.seckillId = seckillId;
	}
	
   	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer  money) {
		this.money = money;
	}
	
   	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer  userId) {
		this.userId = userId;
	}
	
   	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date  createTime) {
		this.createTime = createTime;
	}
	
   	public java.util.Date getPayTime() {
		return payTime;
	}
	public void setPayTime(java.util.Date  payTime) {
		this.payTime = payTime;
	}
	
   	public String getStatus() {
		return status;
	}
	public void setStatus(String  status) {
		this.status = status;
	}
	
   	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String  receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	
   	public String getReceiverMobile() {
		return receiverMobile;
	}
	public void setReceiverMobile(String  receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	
   	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String  receiver) {
		this.receiver = receiver;
	}
	
   	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String  transactionId) {
		this.transactionId = transactionId;
	}
	
}