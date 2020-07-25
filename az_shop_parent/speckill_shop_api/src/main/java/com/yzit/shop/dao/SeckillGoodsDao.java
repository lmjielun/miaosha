package com.yzit.shop.dao;
import java.util.List;
import com.yzit.shop.entity.SeckillGoods;
import org.springframework.stereotype.Repository;

/**
 * 
 * <br>
 * <b>功能：</b>秒杀商品-持久层<br>
 * <b>作者：</b>Administrator<br>
 * <b>日期：</b> 2020-06-12 <br>
 * <b>版权所有： 2020，云优众<br>
 */
@Repository
public interface SeckillGoodsDao  {
	
	/**
	 * 保持对象
	 * 
	 * @param seckillGoods
	 */
	public int save(SeckillGoods seckillGoods);

	/**
	 * 根据id删除对象
	 * 
	 * @param id
	 */
	public int delById(Long id);

	/**
	 * 修改对象
	 * 
	 * @param seckillGoods
	 */
	public int update(SeckillGoods seckillGoods);

	/**
	 * 检索所有的对象
	 * 
	 * @return
	 */
	public List<SeckillGoods> findAll();

	/**
	 * 根据条件检索对象
	 * 
	 * @param seckillGoods
	 * @return
	 */
	public List<SeckillGoods> findByList(SeckillGoods seckillGoods);

	/**
	 * 根据id检索对象
	 * 
	 * @param id
	 * @return
	 */
	public SeckillGoods  findById(Integer id);
}