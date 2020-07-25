package com.yzit.shop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Supplier;
import com.sun.jersey.core.provider.jaxb.AbstractJAXBElementProvider;
import com.yzit.config.Constant;
import com.yzit.framework.web.ui.AjaxResult;

import com.yzit.shop.entity.SeckillGoods;
import com.yzit.shop.entity.SeckillStatus;
import com.yzit.shop.service.SeckillGoodsService;
import com.yzit.shop.task.MultiThreadingCreateOrder;
import com.yzit.shop.uitl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yzit.shop.entity.SeckillOrder;
import com.yzit.shop.service.SeckillOrderService;

import javax.ws.rs.GET;

@RestController
@RequestMapping("seckillOrder")
@CrossOrigin
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;


    /**
     *  创建订单，参数只是用户的Id和商品的id
     * @param userId
     * @param goodsId
     * @return
     */
    @GetMapping("/addOrder")
    public AjaxResult addOrder(Integer userId , Integer goodsId){

        try {
            /**
             *  超卖控制
             *  从redis的队列中获取到商品sid集合
             */
            Object ids = redisTemplate.opsForList().rightPop("SeckillGoodsCountList_" + goodsId);
            System.out.println("redis获取到的ids = " + ids);
            if(ids == null){
                return AjaxResult.ERROR("库存不足");
            }

            /**
             * 1、根据Redis里的  key值，和item字段 查询到商品
             * key值：存储的是时候用的是LoadSpeckillGoodsTask类里的静态值，
             * 迁移到Constant类里一份，两个的值是一样的，想用哪个类里的就用哪个类里
             * item字段：存储的时候用的是 商品的id
             */
            SeckillGoods goods = (SeckillGoods)redisTemplate.opsForHash().get(Constant.SECKILL_KEY, goodsId);

            /**
             *  校验库存
             */
            if(goods != null && goods.getStockCount() > 0){

                /**
                 *  用户排队：利用自增特性，线程安全
                 *  当用户第一次排队，给个标识值 为1  第二次排队+1 第三次再加+1
                 *  判断用户的 标识值，如果大于1了，说明重复排队
                 */
                Long userQueueCount = redisTemplate.opsForHash().increment(Constant.USER_QUEUE_COUNT, userId, 1);
                if(userQueueCount > 1){
                    System.out.println("已经下过单了");
                    return AjaxResult.ERROR("已经下过单了");
                }

                /**
                 *  创建订单排队对象，然后将订单排队对象存储到redis的list中
                 *  需要用户id 商品id 和 时间
                 */
                SeckillStatus seckillStatus = new SeckillStatus(userId,new Date(),goodsId);
                // 左侧进入
                redisTemplate.opsForList().leftPush(Constant.SECKILL_ORDER_QUEUE_KEY,seckillStatus);

                /**
                 *  用户抢单状态存入redis中
                 *  key: USER_QUEUE_STATUS_KEY = "UserQueueStatus";
                 *  item:用户的Id
                 *  value:订单排队对象
                 */
                redisTemplate.opsForHash().put(Constant.USER_QUEUE_STATUS_KEY,userId,seckillStatus);


                /**
                 *  调用多线程，创建订单
                 *  修改多线程的addOrder方法，不需要传递参数了
                 *  直接从redis中获取订单排队对象
                 */
                multiThreadingCreateOrder.addOrder();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR(e.getMessage());
        }
        return AjaxResult.OK();
    }


    /**
     *  根据用户编号查询用户 订单状态对象
     */
    @GetMapping("/queryStatus/{userId}")
    public SeckillStatus queryStatus(@PathVariable Integer userId){
        // 调用redis查询
        SeckillStatus seckillStatus =(SeckillStatus) redisTemplate.opsForHash().get(Constant.USER_QUEUE_STATUS_KEY,userId);

        if(seckillStatus == null){
            seckillStatus = new SeckillStatus(userId,new Date(),null);
        }

        return seckillStatus;
    }


    /**
     *  根据用户编号查询用户 订单信息
     */
    @GetMapping(value="/order/{userId}",produces = "application/json;charset=UTF-8")
    public  SeckillOrder  queryOrder(@PathVariable Integer userId){
        // 根据用户编号，查询用户的订单信息 返回一个订单信息
        SeckillOrder order = (SeckillOrder)redisTemplate.opsForHash().get(Constant.SECKILL_ORDER_KEY, userId);
        return order;
    }

    /**
     *  修改订单状态
     *  orderNo 秒杀的订单编号Id
     *  transactionId 支付宝的订单号
     */
   /* @GetMapping(value="/updateOrderStatus",produces = "application/json;charset=UTF-8")
    public AjaxResult updateOrderStatus(String  orderNo , String  transactionId){
        try {
            // 因为这里没办法获取到用户的id，所以只能查询到所有的秒杀订单，循环遍历，比对订单号相等就是这个用户秒杀的订单了
            List<SeckillOrder> orderList = redisTemplate.opsForHash().values(Constant.SECKILL_ORDER_KEY);

            SeckillOrder seckillOrder = null;
            if(orderList != null && !orderList.isEmpty()){
                // 循环比对order
                for(SeckillOrder order : orderList){
                    // 如果传递过来的orderNo 与 orderList循环到这的订单号相同,说明就是要找到订单对象
                    if( orderNo == order.getId() || orderNo.equals(order.getId())){
                        System.out.println("支付宝获取的订单号=="+ orderNo);
                        seckillOrder = order;
                        break;//跳出循环
                    }
                }
            }
            *//**
             *  修改订单状态，同步到数据库
             *//*
            if(seckillOrder != null){

                seckillOrder.setPayTime(new Date());// 支付时间
                seckillOrder.setStatus("1");// 订单状态 1为 支付过
                seckillOrder.setTransactionId(transactionId);// 支付宝流水号，支付宝订单编号
                // 保存到数据库
                seckillOrderService.save(seckillOrder);

                *//**
                 *  删除redis中的对应的数据
                 *//*
                // 删除当前用户的订单
                redisTemplate.opsForHash().delete(Constant.SECKILL_ORDER_KEY,seckillOrder.getUserId());

                // 删除当前用户排队的信息
                redisTemplate.opsForHash().delete(Constant.USER_QUEUE_COUNT,seckillOrder.getUserId());

                // 删除当前用户的抢单状态对象
                redisTemplate.opsForHash().delete(Constant.USER_QUEUE_STATUS_KEY,seckillOrder.getUserId());
            }
            return AjaxResult.OK();
        } catch (Exception e) {
            e.printStackTrace();
            return  AjaxResult.ERROR(e.getMessage());
        }
    }*/

    /**
     *  修改订单状态
     *  orderNo 秒杀的订单编号Id
     *  transactionId 支付宝的订单号
     *  使用fegin远程调用，get传递多个参数，需要服务端提供参数，指定参数名称
     */
    @GetMapping(value="/updateOrderStatus",produces = "application/json;charset=UTF-8")
    public AjaxResult updateOrderStatus(@RequestParam("orderNo") String  orderNo , @RequestParam("transactionId") String  transactionId){
        try {
            // 因为这里没办法获取到用户的id，所以只能查询到所有的秒杀订单，循环遍历，比对订单号相等就是这个用户秒杀的订单了
            List<SeckillOrder> orderList = redisTemplate.opsForHash().values(Constant.SECKILL_ORDER_KEY);

            SeckillOrder seckillOrder = null;
            if(orderList != null && !orderList.isEmpty()){
                // 循环比对order
                for(SeckillOrder order : orderList){
                    // 如果传递过来的orderNo 与 orderList循环到这的订单号相同,说明就是要找到订单对象
                    if( orderNo == order.getId() || orderNo.equals(order.getId())){
                        System.out.println("支付宝获取的订单号=="+ orderNo);
                        seckillOrder = order;
                        break;//跳出循环
                    }
                }
            }
            /**
             *  修改订单状态，同步到数据库
             */
            if(seckillOrder != null){

                seckillOrder.setPayTime(new Date());// 支付时间
                seckillOrder.setStatus("1");// 订单状态 1为 支付过
                seckillOrder.setTransactionId(transactionId);// 支付宝流水号，支付宝订单编号
                // 保存到数据库
                seckillOrderService.save(seckillOrder);

                /**
                 *  删除redis中的对应的数据
                 */
                // 删除当前用户的订单
                redisTemplate.opsForHash().delete(Constant.SECKILL_ORDER_KEY,seckillOrder.getUserId());

                // 删除当前用户排队的信息
                redisTemplate.opsForHash().delete(Constant.USER_QUEUE_COUNT,seckillOrder.getUserId());

                // 删除当前用户的抢单状态对象
                redisTemplate.opsForHash().delete(Constant.USER_QUEUE_STATUS_KEY,seckillOrder.getUserId());
            }
            return AjaxResult.OK();
        } catch (Exception e) {
            e.printStackTrace();
            return  AjaxResult.ERROR(e.getMessage());
        }
    }

    /**
     * 返回缓存中所有的订单信息
     * @return
     */
    @GetMapping("/all")
    public List<SeckillOrder>  findOrderListByUserId(){
        return  redisTemplate.opsForHash().values(Constant.SECKILL_ORDER_KEY);
    }

    /**
     * 分页 方法
     * @param seckillOrder
     * @return
     */
    @GetMapping("/page")
    public PageInfo<SeckillOrder> page(SeckillOrder seckillOrder  ){
        PageHelper.startPage( seckillOrder.getPageNo(),seckillOrder.getPageSize()  );

        List<SeckillOrder> seckillOrderList = seckillOrderService.findByList(seckillOrder);
        PageInfo<SeckillOrder> pageInfo  = new PageInfo<SeckillOrder>(seckillOrderList);
        return pageInfo;
    }

    /**
     * 查询所有的秒杀订单
     * @return
     */
    @GetMapping("/list")
    public List<SeckillOrder> list(){
       return  seckillOrderService.findAll();
    }

    /**
     * 根据id查询秒杀订单对象
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public SeckillOrder findById(@PathVariable String  id){
       return  seckillOrderService.findById(id);
    }

    /**
     * 添加秒杀订单
     * @param seckillOrder
     * @return
     */
    @PostMapping("/seckillOrder")
    public AjaxResult save( @RequestBody SeckillOrder seckillOrder   ){
       int count =  seckillOrderService.save(seckillOrder);
       if(count > 0 ){
	       return AjaxResult.OK();
       }
       return AjaxResult.ERROR("新增秒杀订单失败");
    }

    /**
     * 修改秒杀订单
     * @param seckillOrder
     * @return
     */
    @PutMapping("/seckillOrder")
    public AjaxResult update( @RequestBody  SeckillOrder seckillOrder){
        int count =seckillOrderService.update(seckillOrder);

        if(count > 0 ){
            return AjaxResult.OK();
        }
        return AjaxResult.ERROR("修改秒杀订单失败");
    }
    @DeleteMapping("/{id}")
    public AjaxResult  del( @PathVariable Long id  ){
        int count = seckillOrderService.del(id);
        if(count > 0 ){
         	return AjaxResult.OK();
        }
        return AjaxResult.ERROR("删除秒杀订单失败");
    }
    @DeleteMapping("/batch/{ids}")
    public AjaxResult  batchDel(@PathVariable Long []  ids  ){
       boolean   success =  seckillOrderService.batchDel(ids);
       if(success){//删除成功
           return AjaxResult.OK();
       }
        return AjaxResult.ERROR("批量删除秒杀订单失败");
    }
}