package com.yzit.shop.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import com.yzit.shop.entity.SeckillGoods;
import com.yzit.shop.dao.SeckillGoodsDao;

/**
 * 
 * <br>
 * <b>功能：</b>秒杀商品--服务实现类<br>
 * <b>作者：</b>Administrator<br>
 * <b>日期：</b> 2020-06-12 <br>
 * <b>版权所有： 2020，云优众<br>
 */ 
@Service("seckillGoodsService")
@Transactional
public class SeckillGoodsServiceImpl   implements SeckillGoodsService {
	//private final static Logger log= Logger.getLogger(SeckillGoodsService.class);
	
	@Autowired
	protected SeckillGoodsDao  seckillGoodsDao;

	/**
	 * 保持对象
	 * 
	 * @param seckillGoods
	 */
	public int save(SeckillGoods  seckillGoods){
		return	seckillGoodsDao.save(seckillGoods);
	}

	/**
	 * 根据id删除对象
	 * 
	 * @param id
	 */
	public int del(Long id){
		return seckillGoodsDao.delById(id);
	}

	/**
	 * 修改对象
	 * 
	 * @param seckillGoods
	 */
	 
	public int update(SeckillGoods  seckillGoods){
		return seckillGoodsDao.update(seckillGoods);
	}

	/**
	 * 检索所有的对象
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<SeckillGoods> findAll(){
		return seckillGoodsDao.findAll();
	}

	/**
	 * 根据条件检索对象
	 * 
	 * @param seckillGoods
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<SeckillGoods> findByList(SeckillGoods  seckillGoods){
		return seckillGoodsDao.findByList(seckillGoods);
	}

	/**
	 * 根据id检索对象
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public SeckillGoods  findById(Integer id){
		return seckillGoodsDao.findById(id);
	}

	/**
     * 批量删除信息
     * 
     * @param ids 需要删除的ID集合
     * @return 结果
     */
   public boolean batchDel(Long[] ids){
        if(ids != null && ids.length > 0){
            for(Long id : ids){
                seckillGoodsDao.delById(id);
            }
        }
        return true;
    }
}
