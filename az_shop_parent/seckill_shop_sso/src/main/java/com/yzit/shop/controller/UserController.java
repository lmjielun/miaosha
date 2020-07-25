package com.yzit.shop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yzit.framework.web.ui.AjaxResult;
import com.yzit.shop.entity.User;
import com.yzit.shop.service.UserService;
import com.yzit.util.MD5Util2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    // 注入redis
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *  去登录 存储到redis
     * @param user
     * @return
     */
    @PostMapping(value = "/login",produces = "application/json; charset=utf-8")
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

    /**
     *  根据token查询用户
     * @param token
     * @return
     */
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
    }


    /**
     * 分页 方法
     * @param user
     * @return
     */
    @GetMapping("/page")
    public PageInfo<User> page(User user  ){
        PageHelper.startPage( user.getPageNo(),user.getPageSize()  );

        List<User> userList = userService.findByList(user);
        PageInfo<User> pageInfo  = new PageInfo<User>(userList);
        return pageInfo;
    }

    /**
     * 查询所有的用户表
     * @return
     */
    @GetMapping("/list")
    public List<User> list(){
       return  userService.findAll();
    }

    /**
     * 根据id查询用户表对象
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public User findById(@PathVariable Long  id){
       return  userService.findById(id);
    }

    /**
     * 添加用户表
     * @param user
     * @return
     */
    @PostMapping("/user")
    public AjaxResult save( @RequestBody User user   ){
       int count =  userService.save(user);
       if(count > 0 ){
	       return AjaxResult.OK();
       }
       return AjaxResult.ERROR("新增用户表失败");
    }

    /**
     * 修改用户表
     * @param user
     * @return
     */
    @PutMapping("/user")
    public AjaxResult update( @RequestBody  User user){
        int count =userService.update(user);

        if(count > 0 ){
            return AjaxResult.OK();
        }
        return AjaxResult.ERROR("修改用户表失败");
    }
    @DeleteMapping("/{id}")
    public AjaxResult  del( @PathVariable Long id  ){
        int count = userService.del(id);
        if(count > 0 ){
         	return AjaxResult.OK();
        }
        return AjaxResult.ERROR("删除用户表失败");
    }
    @DeleteMapping("/batch/{ids}")
    public AjaxResult  batchDel(@PathVariable Long []  ids  ){
       boolean   success =  userService.batchDel(ids);
       if(success){//删除成功
           return AjaxResult.OK();
       }
        return AjaxResult.ERROR("批量删除用户表失败");
    }
}