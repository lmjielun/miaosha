package com.yzit.shop.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yzit.framework.web.ui.BaseEntity;

import java.util.Date;

/**
 * 
 * <br>
 * <b>功能：</b>秒杀商品实体类<br>
 * <b>作者：</b>Administrator<br>
 * <b>日期：</b> 2020-06-12 <br>
 * <b>版权所有： 2020，云优众<br>
 */ 
public class SeckillGoods extends BaseEntity {
   private static final long serialVersionUID = 1L;
    private Integer  id;//
    private Long  goodsId;//spu ID
    private Long  itemId;//sku ID
    private String  title;//标题
    private String  smallPic;//商品图片
    private Integer  price;//原价格
    private Integer  costPrice;//秒杀价格
    private String  sellerId;//商家ID
    private java.util.Date  createTime;//添加日期
    private java.util.Date  checkTime;//审核日期
    private String  status;//审核状态 0 未审核 1审核通过 2 审核不通过
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private java.util.Date  startTime;//开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private java.util.Date  endTime;//结束时间
    private Integer  num;//秒杀商品数
    private Integer  stockCount;//剩余库存数
    private String  introduction;//描述

	// 扩展属性 当前时间
	private Date currentTime;

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer  id) {
		this.id = id;
	}
	
   	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long  goodsId) {
		this.goodsId = goodsId;
	}
	
   	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long  itemId) {
		this.itemId = itemId;
	}
	
   	public String getTitle() {
		return title;
	}
	public void setTitle(String  title) {
		this.title = title;
	}
	
   	public String getSmallPic() {
		return smallPic;
	}
	public void setSmallPic(String  smallPic) {
		this.smallPic = smallPic;
	}
	
   	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer  price) {
		this.price = price;
	}
	
   	public Integer getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Integer  costPrice) {
		this.costPrice = costPrice;
	}
	
   	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String  sellerId) {
		this.sellerId = sellerId;
	}
	
   	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date  createTime) {
		this.createTime = createTime;
	}
	
   	public java.util.Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(java.util.Date  checkTime) {
		this.checkTime = checkTime;
	}
	
   	public String getStatus() {
		return status;
	}
	public void setStatus(String  status) {
		this.status = status;
	}
	
   	public java.util.Date getStartTime() {
		return startTime;
	}
	public void setStartTime(java.util.Date  startTime) {
		this.startTime = startTime;
	}
	
   	public java.util.Date getEndTime() {
		return endTime;
	}
	public void setEndTime(java.util.Date  endTime) {
		this.endTime = endTime;
	}
	
   	public Integer getNum() {
		return num;
	}
	public void setNum(Integer  num) {
		this.num = num;
	}
	
   	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer  stockCount) {
		this.stockCount = stockCount;
	}
	
   	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String  introduction) {
		this.introduction = introduction;
	}
	
}