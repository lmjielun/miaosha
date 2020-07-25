package com.yzit.shop.task;

import com.yzit.config.Constant;
import com.yzit.shop.entity.SeckillGoods;
import com.yzit.shop.service.SeckillGoodsService;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component // 交给spring管理
public class LoadSpeckillGoodsTask {


    // 注入redisTemplate
    @Autowired
    private RedisTemplate redisTemplate;

    // 注入秒杀的service
    @Autowired
    private SeckillGoodsService seckillGoodsService;

    // 存入redis 的hash的key  静态的，方便其他类调用
    public  static String SECKILL_KEY = "seckillList";

    /**
     *  每30秒执行一次
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void process(){

        /**
         *  根据秒杀条件，查询秒杀商品
         *  条件：
         *      1、状态  为1
         *      2、时间  秒杀开始时间 < 当前时间 < 秒杀结束时间
         *          时间实现思路：
         *                  1、实体类添加一个当前时间属性，接收当前时间，修改mapper.xml查询条件即可
         *                  2、使用mysql的now(),写sql语句获取当前时间，不需要传递参数
         */

        // 1、创建一个秒杀对象，作为查询条件
        SeckillGoods seckillGoods = new SeckillGoods();
        // 2、设置秒杀对象的 状态为1
        seckillGoods.setStatus("1");
        // 3、设置秒杀对象的 当前时间
        seckillGoods.setCurrentTime(new Date());
        // 4、调用秒杀的service接口的findByList方法，将秒杀对象传入进去，获取到秒杀对象List集合
        List<SeckillGoods> goodsList = seckillGoodsService.findByList(seckillGoods);
        //redisTemplate.delete(Constant.SECKILL_GOODCOUNT);

        // 清空数据
        List<SeckillGoods> redisGoodsList = redisTemplate.opsForHash().values(SECKILL_KEY);

        if(redisGoodsList != null && !redisGoodsList.isEmpty()){
            for(SeckillGoods redisGoods : redisGoodsList){
                redisTemplate.opsForHash().delete(SECKILL_KEY,redisGoods.getId(),redisGoods);
            }
        }

        // 5、将获取到的秒杀对象集合，将集合中的对象，一一存入redis的hash中
            // 为什么不直接使用string存入redids?
            //商品的详情页面需要根据商品编号查询某一个商品对象
            //下单判断库存，也需要根据商品的编号获取某一个商品对象
        //5.1 判断list集合是否为空 null是指对象本身没分配内存，isEmpty是指这个对象指象的内在单元中没有数据
        if(goodsList != null && !goodsList.isEmpty() ){
            // 5.2 for循环得到的list集合
            for(SeckillGoods goods : goodsList){

                    // 5.3 调用redisTemplate存入put 到 hash中
                    redisTemplate.opsForHash().put(SECKILL_KEY,goods.getId(),goods);

                    // 5.4 创建秒杀商品队列，调用本类方法的pushIds()，存入库存数，以及商品Id,得到Int数组
                    Integer[] goodsIds = this.pushIds(goods.getStockCount(),goods.getId());

                    if(goodsIds != null){
                        // 清空队列
                        redisTemplate.delete(Constant.SECKILL_GOODCOUNT_List+goods.getId());

                        // 将数组一次性存入到redis中,千万不要一次性存到redis会报错，要一条一条存
                        //redisTemplate.opsForList().leftPushAll(Constant.SECKILL_GOODCOUNT_List + goods.getId(),goodsIds);
                        for(Integer id : goodsIds){
                            redisTemplate.opsForList().leftPush(Constant.SECKILL_GOODCOUNT_List+goods.getId(),id);
                        }

                        // 使用自增计数器，从计数器中获取是否还有库存
                        Long increment = redisTemplate.opsForHash().increment(Constant.SECKILL_GOODCOUNT, goods.getId(), goods.getStockCount());

                        // 每30s定时器会获取一次数据库中商品库存数，所以这里要每30s减去一次库存数，不然会叠加倍增
                        if(increment > goods.getStockCount()){
                            redisTemplate.opsForHash().increment(Constant.SECKILL_GOODCOUNT, goods.getId(), -goods.getStockCount());
                        }
                    }
            }
        }

        System.out.println("定时器执行 ： "+(System.currentTimeMillis()));
    }


    /**
     *  添加商品队列
     *  int len : 为数组长度
     *  Integer id： 为商品的Id
     */
    public Integer[] pushIds(int len , Integer id){
        Integer[] ids = new Integer[len];
        for(int i=0; i<ids.length;i++){
            ids[i] = id;
        }
        return ids;
    }



}
