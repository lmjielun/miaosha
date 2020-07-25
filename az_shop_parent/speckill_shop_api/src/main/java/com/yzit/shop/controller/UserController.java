package com.yzit.shop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yzit.framework.web.ui.AjaxResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.yzit.shop.entity.User;
import com.yzit.shop.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

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