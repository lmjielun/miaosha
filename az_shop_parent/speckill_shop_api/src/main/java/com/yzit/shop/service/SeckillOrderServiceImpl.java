package com.yzit.shop.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import com.yzit.shop.entity.SeckillOrder;
import com.yzit.shop.dao.SeckillOrderDao;

/**
 * 
 * <br>
 * <b>功能：</b>秒杀订单--服务实现类<br>
 * <b>作者：</b>Administrator<br>
 * <b>日期：</b> 2020-06-12 <br>
 * <b>版权所有： 2020，云优众<br>
 */ 
@Service("seckillOrderService")
@Transactional
public class SeckillOrderServiceImpl   implements SeckillOrderService {
	//private final static Logger log= Logger.getLogger(SeckillOrderService.class);
	
	@Autowired
	protected SeckillOrderDao  seckillOrderDao;

	/**
	 * 保持对象
	 * 
	 * @param seckillOrder
	 */
	public int save(SeckillOrder  seckillOrder){
		return	seckillOrderDao.save(seckillOrder);
	}

	/**
	 * 根据id删除对象
	 * 
	 * @param id
	 */
	public int del(Long id){
		return seckillOrderDao.delById(id);
	}

	/**
	 * 修改对象
	 * 
	 * @param seckillOrder
	 */
	 
	public int update(SeckillOrder  seckillOrder){
		return seckillOrderDao.update(seckillOrder);
	}

	/**
	 * 检索所有的对象
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<SeckillOrder> findAll(){
		return seckillOrderDao.findAll();
	}

	/**
	 * 根据条件检索对象
	 * 
	 * @param seckillOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<SeckillOrder> findByList(SeckillOrder  seckillOrder){
		return seckillOrderDao.findByList(seckillOrder);
	}

	/**
	 * 根据id检索对象
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public SeckillOrder  findById(String id){
		return seckillOrderDao.findById(id);
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
                seckillOrderDao.delById(id);
            }
        }
        return true;
    }
}
