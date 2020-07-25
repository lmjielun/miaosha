package com.yzit.shop.task;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.yzit.config.Constant;
import com.yzit.framework.web.ui.AjaxResult;
import com.yzit.shop.entity.SeckillGoods;
import com.yzit.shop.entity.SeckillOrder;
import com.yzit.shop.entity.SeckillStatus;
import com.yzit.shop.service.SeckillGoodsService;
import com.yzit.shop.uitl.IdWorker;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.aspectj.apache.bcel.classfile.ConstantInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Date;

/**
 *  多线程类
 *  将订单controller类的if内部的代码，也就是创建订单的代码迁移过来
 *  然后订单controller类里If内部的代码删除 调用该类的方法
 */

@Component // 交给spring管理
public class MultiThreadingCreateOrder {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Async   //交给spring的taskExcutor任务执行器，使用多线程异步执行
    public void  addOrder() {

        System.out.println("准备执行....");
            /**
             * 1、从redis的list队列中直接获取 订单排队对象
             *    然后从 订单排队对象 中 取出 goodsId  和 userId
             */
            SeckillStatus seckillStatus=(SeckillStatus)redisTemplate.opsForList().rightPop(Constant.SECKILL_ORDER_QUEUE_KEY);
            Integer userId = seckillStatus.getUserId();
            Integer goodsId = seckillStatus.getGoodsId();

             /**
             * 2、根据goodsId,从redis中取出 秒杀的商品
             */
            SeckillGoods  goods = (SeckillGoods)redisTemplate.opsForHash().get(Constant.SECKILL_KEY, goodsId);


            /**
             * 3、进行下单，存储秒杀订单
             */
            if(goods != null && goods.getStockCount() > 0){
                // 雪花算法得到订单的主键id
                long orderId = idWorker.nextId();

                // 创建订单对象
                SeckillOrder seckillOrder = new SeckillOrder(orderId+"", goodsId, goods.getCostPrice(), userId, new Date(), "0");
                System.out.println("多线程下单的订单号 ： " + seckillOrder.getId());

                // 将订单对象，存储到redis中
                redisTemplate.opsForHash().put(Constant.SECKILL_ORDER_KEY,userId,seckillOrder);


                /**
                 *  更改用户的 订单排队对象的状态
                 *      1、订单编号
                 *      2、支付金额
                 *      3、状态 1:排队中，2:秒杀等待支付,3:支付超时，4:秒杀失败,5:支付完成
                 */
                seckillStatus.setOrderId(Long.parseLong(seckillOrder.getId())); // 订单编号
                seckillStatus.setMoney(seckillOrder.getMoney());// 支付金额
                seckillStatus.setStatus(2);// 状态 2:秒杀等待支付
                // 然后将该用户的  订单排队对象 存储到redis中
                redisTemplate.opsForHash().put(Constant.USER_QUEUE_STATUS_KEY,userId,seckillStatus);

                /**
                 * 4、减少库存
                 *  从redis获取库存自增计数器，每次操作订单都需要操作自增器，自增器是 -1 ，每次操作都是库存 - 1
                 */
                Long seckillGoodsCount = redisTemplate.opsForHash().increment(Constant.SECKILL_GOODCOUNT, goods.getId(),-1);
                // 设置消减秒杀商品的
                goods.setStockCount(seckillGoodsCount.intValue());
                System.out.println(seckillGoodsCount.intValue() + "==下单后商品库存");
                /**
                 *  4.1 发送延时消息
                 */
                // 4.2 将秒杀订单对象转为json字符串
                String OrderJson = JSON.toJSONString(seckillOrder);

                // 4.3 创建消息名称，指定消息目标
                Destination destination = new ActiveMQQueue("delay.queue.seckillOrder");
                // 4.4 使用jmsTemplate，将消息名称放入进去，创建一个消息，发送延迟消息+
                jmsTemplate.send(destination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        // 4.5 使用session创建消息，将转换好的秒杀订单对象，传递过去
                        TextMessage textMessage = session.createTextMessage(OrderJson);
                        // 4.6 设置延迟时间
                        long time =   30000;
                        textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,time);

                        // 4.6 返回textMessage
                        return textMessage;
                    }
                });


                // 判断 库存
                if(seckillGoodsCount.intValue() <= 0){
                    // 将数据同步到mysql中，并且清空redis缓存中的该商品
                    seckillGoodsService.update(goods);
                    redisTemplate.opsForHash().delete(Constant.SECKILL_KEY,goodsId);
                    redisTemplate.delete(Constant.SECKILL_GOODCOUNT_List+goods.getId());
                }else{
                    // 如果有库存，重置redis该商品的数量
                    redisTemplate.opsForHash().put(Constant.SECKILL_KEY,goodsId,goods);

                }
            }else{
                throw new RuntimeException("已售罄!");
            }

        System.out.println("开始执行....");
    }

    /**
     *  清除用户排队信息
     * @param seckillStatus
     */
    public void clearQueue(SeckillStatus seckillStatus){
        // 清除重复排队标识
        redisTemplate.opsForHash().delete(Constant.USER_QUEUE_COUNT,seckillStatus.getUserId());
        // 清理排队存储信息
        redisTemplate.opsForHash().delete(Constant.USER_QUEUE_STATUS_KEY,seckillStatus.getUserId());
    }


    @Async   //交给spring的taskExcutor任务执行器，使用多线程异步执行
    public void  addOrder(Integer  userId ,Integer  goodsId) {

        try {
            /**
             * 1、根据Redis里的  key值，和item字段 查询到商品
             * key值：存储的是时候用的是LoadSpeckillGoodsTask类里的静态值，
             * 迁移到Constant类里一份，两个的值是一样的，想用哪个类里的就用哪个类里
             * item字段：存储的时候用的是 商品的id
             */
            SeckillGoods goods = (SeckillGoods)redisTemplate.opsForHash().get(Constant.SECKILL_KEY, goodsId);

            /**
             *  2、创建一个订单对象
             *  需要设置 商品的id、用户的id、库存、下单时间、订单状态，到实体类中，设置下构造方法
             */

            // 使用雪花算法 作为订单id 解决高并发、冲突问题
            long orderId = idWorker.nextId();

            SeckillOrder seckillOrder = new SeckillOrder(orderId+"",goodsId,goods.getCostPrice(),userId,new Date(),"0");
            System.out.println(seckillOrder.getId()+"多线程下单订单号："+orderId+"");
            /**
             * 3、将订单对象存入redis中
             *  key值为：Constant类里的常量SECKILL_ORDER_KEY
             *  itme字段为：userId用户的id
             *  value值为：seckillOrder 订单对象
             */
            redisTemplate.opsForHash().put(Constant.SECKILL_ORDER_KEY,userId,seckillOrder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
