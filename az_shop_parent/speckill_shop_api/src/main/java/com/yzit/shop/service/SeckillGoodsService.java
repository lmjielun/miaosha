package com.yzit.shop.service;
import java.util.List;
import com.yzit.shop.entity.SeckillGoods;
//import com.github.pagehelper.PageInfo;
/**
 * 
 * <br>
 * <b>功能：</b>秒杀商品--服务实现类<br>
 * <b>作者：</b>Administrator<br>
 * <b>日期：</b> 2020-06-12 <br>
 * <b>版权所有： 2020，云优众<br>
 */ 
public interface SeckillGoodsService {
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
	public int del(Long id);

	/**
	 * 修改对象
	 * 
	 * @param seckillGoods
	 */
	public int  update(SeckillGoods seckillGoods);

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

	
	/**
     * 批量删除信息
     * 
     * @param ids 需要删除的ID集合
     * @return 结果
     */
    public boolean batchDel(Long[] ids) ;
}