package com.yzit.shop.service;

import com.yzit.shop.dao.UserDao;
import com.yzit.shop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * <br>
 * <b>功能：</b>用户表--服务实现类<br>
 * <b>作者：</b>Administrator<br>
 * <b>日期：</b> 2020-06-12 <br>
 * <b>版权所有： 2020，云优众<br>
 */ 
@Service("userService")
@Transactional
public class UserServiceImpl   implements UserService {
	//private final static Logger log= Logger.getLogger(UserService.class);



	@Autowired
	protected UserDao  userDao;

	@Override
	public User doLogin(User user) {
		return userDao.doLogin(user);
	}


	/**
	 * 保持对象
	 * 
	 * @param user
	 */
	public int save(User  user){
		return	userDao.save(user);
	}

	/**
	 * 根据id删除对象
	 * 
	 * @param id
	 */
	public int del(Long id){
		return userDao.delById(id);
	}

	/**
	 * 修改对象
	 * 
	 * @param user
	 */
	 
	public int update(User  user){
		return userDao.update(user);
	}

	/**
	 * 检索所有的对象
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<User> findAll(){
		return userDao.findAll();
	}

	/**
	 * 根据条件检索对象
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<User> findByList(User  user){
		return userDao.findByList(user);
	}

	/**
	 * 根据id检索对象
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public User  findById(Long id){
		return userDao.findById(id);
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
                userDao.delById(id);
            }
        }
        return true;
    }
}
