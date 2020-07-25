package com.yzit.shop.controller;
import com.yzit.framework.web.ui.AjaxResult;
import com.yzit.shop.entity.SeckillGoods;
import com.yzit.shop.entity.User;
import com.yzit.shop.service.UserService;
import com.yzit.shop.task.LoadSpeckillGoodsTask;
import com.yzit.util.MD5Util2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.events.AliasEvent;
import sun.security.provider.MD5;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 *  秒杀服务接口，提供功能
 *  1、提供秒杀商品的list集合，每页返回12条记录
 *  2、提供根据秒杀商品的id查询 秒杀商品对象
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/seckill")
public class SeckillGoodsAPI {

    // 注入redis
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     *  根据redis存入的key 获取到秒杀商品的集合
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list",produces = "application/json; cahrset=utf-8")
    public List<SeckillGoods> list(@RequestParam(defaultValue = "1") int pageNo,
                                   @RequestParam(defaultValue = "12") int pageSize){

        // 调用redis 根据Key值获取该key下的集合 key值是LoadSpecKillGoodsTask类里的静态变量

        List list = redisTemplate.opsForHash().values(LoadSpeckillGoodsTask.SECKILL_KEY);
        return list;

    }


    /**
     *  根据redis存入的item字段，当时字段存储的就是商品的id，获取到秒杀商品对象
     * @param id
     * @return
     */
    @GetMapping(value = "{id}",produces = "application/json; charset=utf-8")
    public SeckillGoods findById(@PathVariable Integer id){

        // 根据Key 和 item字段 获取到一个秒杀商品对象
        SeckillGoods seckillGoods = (SeckillGoods)redisTemplate.opsForHash().get(LoadSpeckillGoodsTask.SECKILL_KEY,id);

        return seckillGoods;
    }


    /*@PostMapping(value = "/login",produces = "application/json; charset=utf-8")
    public AjaxResult doLogin(@RequestBody User user){

        // 使用md5加密
        String md5 = MD5Util2.encode(user.getPassword(), "MD5");
        user.setPassword(md5);

        // 根据已经加密好的密码 和 用户名 进行查询，并返回结果
        User userDB = userService.doLogin(user);

        if(userDB != null){ // 如果查询到的user有值，存入redis中
            // 创建token令牌
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            // 然后存储到redis中，令牌作为Key值
            redisTemplate.opsForValue().set("USER_INFO_"+token,userDB);
            // 设置存储时间
            redisTemplate.expire("USER_INFO_"+token,4, TimeUnit.HOURS);
            // 将token返回
           return AjaxResult.OK(token);
        }else {
            return AjaxResult.ERROR("账号或密码错误");
        }
    }

    @GetMapping("/token/{token}")
    public AjaxResult checkLogin(@PathVariable String token){
        // 拼接redis的key值
        String key = "USER_INFO_"+token;

        // 调用redis查询
        User user  =(User) redisTemplate.opsForValue().get(key);

        // 每次查询都重新设置redis存储的时间
        redisTemplate.expire("USER_INFO_"+token,4, TimeUnit.HOURS);

        // 判断用户是否为Null
        if(user!=null){
            // 如果有值，放入到AjaxResult的data属性里
            return AjaxResult.OK(user);
        }else {
            return AjaxResult.ERROR("未登录");
        }
    }*/





}
