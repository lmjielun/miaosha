package com.yzit.shop.dao;
import java.util.List;
import com.yzit.shop.entity.SeckillOrder;
import org.springframework.stereotype.Repository;

/**
 * 
 * <br>
 * <b>功能：</b>秒杀订单-持久层<br>
 * <b>作者：</b>Administrator<br>
 * <b>日期：</b> 2020-06-12 <br>
 * <b>版权所有： 2020，云优众<br>
 */
@Repository
public interface SeckillOrderDao  {
	
	/**
	 * 保持对象
	 * 
	 * @param seckillOrder
	 */
	public int save(SeckillOrder seckillOrder);

	/**
	 * 根据id删除对象
	 * 
	 * @param id
	 */
	public int delById(Long id);

	/**
	 * 修改对象
	 * 
	 * @param seckillOrder
	 */
	public int update(SeckillOrder seckillOrder);

	/**
	 * 检索所有的对象
	 * 
	 * @return
	 */
	public List<SeckillOrder> findAll();

	/**
	 * 根据条件检索对象
	 * 
	 * @param seckillOrder
	 * @return
	 */
	public List<SeckillOrder> findByList(SeckillOrder seckillOrder);

	/**
	 * 根据id检索对象
	 * 
	 * @param id
	 * @return
	 */
	public SeckillOrder  findById(String id);
}