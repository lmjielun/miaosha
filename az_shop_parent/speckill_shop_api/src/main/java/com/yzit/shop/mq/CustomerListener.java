package com.yzit.shop.mq;

import com.alibaba.fastjson.JSONArray;
import com.yzit.config.Constant;
import com.yzit.shop.entity.SeckillGoods;
import com.yzit.shop.entity.SeckillOrder;
import com.yzit.shop.service.SeckillGoodsService;
import com.yzit.shop.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *  创建客户端，监听
 */
@Component
public class CustomerListener {

    // 注入秒杀订单Service
    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsService seckillGoodsService;


    /**
     *  监听延迟队列
     */
    @JmsListener(destination = "delay.queue.seckillOrder")
    public synchronized   void receive(String message){ // message就是生产者发送过来的消息
        System.out.println("超时，未支付" + message);
        // 1、转换对象
        // 生产者将 秒杀订单对象转为了 json的Strig类型  这里在转为java对象
        SeckillOrder seckillOrder = JSONArray.parseObject(message, SeckillOrder.class);

        // 2、订单是否下单成功，调用service，根据订单号，查询订单，存在说明成功，否则失败
        SeckillOrder orderDB = seckillOrderService.findById(seckillOrder.getId());
        // 3、判断orderDB是否为Null
        if (orderDB == null) { // 表示下单未成功，支付超时了
            // 1、调用支付宝终止支付接口 todo

            // 2、删除缓存的数据，秒杀订单数据，用户排队信息，用户抢购信息
            // 删除当前用户的订单
            redisTemplate.opsForHash().delete(Constant.SECKILL_ORDER_KEY,seckillOrder.getUserId());

            // 删除当前用户排队的信息，让他可以再买东西
            redisTemplate.opsForHash().delete(Constant.USER_QUEUE_COUNT,seckillOrder.getUserId());

            // 删除当前用户的抢单状态对象
            redisTemplate.opsForHash().delete(Constant.USER_QUEUE_STATUS_KEY,seckillOrder.getUserId());

            // 3、库存回滚
            // 3.1 根据订单编号，查询redis中是否有该商品，如果有了库存回滚
            System.out.println("scekillOrder的id == " +seckillOrder.getId() );
            // 注意这是根据 商品的Id查询 而不是根据 订单的id 不要搞错了
            SeckillGoods goods = (SeckillGoods)redisTemplate.opsForHash().get(Constant.SECKILL_KEY, seckillOrder.getSeckillId());
            System.out.println("查出来的goods = " + goods);
            if(goods != null ){ // redis中有该商品，库存回滚
                // 3.1.1 商品自增器 该商品的数量 + 1
                Long seckillGoodsCount = redisTemplate.opsForHash().increment(Constant.SECKILL_GOODCOUNT, goods.getId(),1);
                // 3.1.2 设置该商品 增加 后的 数量
                goods.setStockCount(seckillGoodsCount.intValue());
                // 3.1.3 将商品重新存储到redis中，因为它下订单的时候，已经减了，他不买了，就在加回来
                redisTemplate.opsForHash().put(Constant.SECKILL_KEY,goods.getId(),goods);
                // 3.1.4 redis中该商品的队列（仓库）加入该商品
                redisTemplate.opsForList().leftPush(Constant.SECKILL_GOODCOUNT_List+goods.getId(),goods.getId());

            }else{ // 缓存中没有查到该商品，那还得将该商品存储到redis中，也许他是最后一件商品，照样得数据回滚
                // 1、读取Mysql数据库，获取到完整的商品信息
                SeckillGoods goodsDB = seckillGoodsService.findById(seckillOrder.getSeckillId());
                System.out.println("goodsDB = " +goodsDB);
                // 我个人认为，只需要修改到数据库即可，因为每30s都会从数据库获取一次值，将所有内容重置。
                if(goodsDB != null){
                    goodsDB.setStockCount(1);
                    seckillGoodsService.update(goodsDB);
                }
              /*  // 2、将完整的信息存储到redis中
                redisTemplate.opsForHash().put(Constant.SECKILL_KEY, goodsDB.getId(),goodsDB);
                // 3、创建队列，队列中存储商品的库存
                Integer[] goodsIds = this.pushIds(goodsDB.getStockCount(),goodsDB.getId());
                // 4、清空队列，这步骤有没有应该无所谓，保险起见清空后，在添加 //todo 这步估计有问题，看是否不要
                redisTemplate.delete(Constant.SECKILL_GOODCOUNT_List+goodsDB.getId());
                // 5、然后在添加到redis的该商品的队列（仓库）中
                redisTemplate.opsForList().leftPushAll(Constant.SECKILL_GOODCOUNT_List+goodsDB.getId());
                // 6、自增器
                redisTemplate.opsForHash().increment(Constant.SECKILL_GOODCOUNT, goodsDB.getId(),goodsDB.getStockCount());*/
            }

        }else {
            // 支付成功 什么都不做
        }
    }

    /**
     *  存储商品所有的Id
     * @param len
     * @param id
     * @return
     */
    public Integer[] pushIds(int len,Integer id){
        Integer[] ids=new Integer[len];
        for(int i=0;i<ids.length;i++){
            ids[i]=id;
        }
        return ids;
    }



}
