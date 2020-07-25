package com.yzit.shop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yzit.framework.web.ui.AjaxResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.yzit.shop.entity.SeckillGoods;
import com.yzit.shop.service.SeckillGoodsService;

@RestController
@RequestMapping("/api/seckillGoods")
@CrossOrigin
public class SeckillGoodsController {

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    /**
     * 分页 方法
     * @param seckillGoods
     * @return
     */
    @GetMapping("/page")
    public PageInfo<SeckillGoods> page(SeckillGoods seckillGoods  ){
        PageHelper.startPage( seckillGoods.getPageNo(),seckillGoods.getPageSize()  );

        List<SeckillGoods> seckillGoodsList = seckillGoodsService.findByList(seckillGoods);
        PageInfo<SeckillGoods> pageInfo  = new PageInfo<SeckillGoods>(seckillGoodsList);
        return pageInfo;
    }

    /**
     * 查询所有的秒杀商品
     * @return
     */
    @GetMapping("/list")
    public List<SeckillGoods> list(){
       return  seckillGoodsService.findAll();
    }

    /**
     * 根据id查询秒杀商品对象
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public SeckillGoods findById(@PathVariable Integer  id){
       return  seckillGoodsService.findById(id);
    }

    /**
     * 添加秒杀商品
     * @param seckillGoods
     * @return
     */
    @PostMapping("/seckillGoods")
    public AjaxResult save( @RequestBody SeckillGoods seckillGoods   ){
       int count =  seckillGoodsService.save(seckillGoods);
       if(count > 0 ){
	       return AjaxResult.OK();
       }
       return AjaxResult.ERROR("新增秒杀商品失败");
    }

    /**
     * 修改秒杀商品
     * @param seckillGoods
     * @return
     */
    @PutMapping("/seckillGoods")
    public AjaxResult update( @RequestBody  SeckillGoods seckillGoods){
        int count =seckillGoodsService.update(seckillGoods);

        if(count > 0 ){
            return AjaxResult.OK();
        }
        return AjaxResult.ERROR("修改秒杀商品失败");
    }
    @DeleteMapping("/{id}")
    public AjaxResult  del( @PathVariable Long id  ){
        int count = seckillGoodsService.del(id);
        if(count > 0 ){
         	return AjaxResult.OK();
        }
        return AjaxResult.ERROR("删除秒杀商品失败");
    }
    @DeleteMapping("/batch/{ids}")
    public AjaxResult  batchDel(@PathVariable Long []  ids  ){
       boolean   success =  seckillGoodsService.batchDel(ids);
       if(success){//删除成功
           return AjaxResult.OK();
       }
        return AjaxResult.ERROR("批量删除秒杀商品失败");
    }
}