package com.yzit.config;


/****
 * 常量类方法
 *   存储常用的字符串，变量
 */
public class Constant {
    /***
     * 秒杀商品列表的key
     */
    public static  String   SECKILL_KEY = "seckillList";
    /***
     * 秒杀订单的key
     */
    public static String  SECKILL_ORDER_KEY = "seckillOrderList";

    /**
     * 秒杀排队Key
     */
    public static String SECKILL_ORDER_QUEUE_KEY = "SeckillOrderQueue";

    /**
     * 用户抢单状态：key
     */
    public static String  USER_QUEUE_STATUS_KEY = "UserQueueStatus";

    /**
     * 用户排队标识  自增值
     */
    public static String USER_QUEUE_COUNT = "UserQueueCount";

    /**
     *  自增商品id
     */
    public static String SECKILL_GOODCOUNT = "SeckillGoodsCount";

    /**
     *  商品id队列
     */
    public static String SECKILL_GOODCOUNT_List = "SeckillGoodsCountList_";

}
