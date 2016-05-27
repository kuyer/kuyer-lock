package io.github.kuyer.lock.api;

/**
 * 锁接口
 * @author Rory.Zhang
 */
public interface LockApi {
	
	/**
	 * 加锁
	 * @param lockId 锁ID（唯一，必填）
	 * @param lockTimeout 锁超时时间（单位：秒）
	 * @return true表示加锁成功；false表示加锁失败
	 */
	public boolean lock(String lockId, Integer lockTimeout);
	
	/**
	 * 解锁
	 * @param lockId 锁ID（唯一，必填）
	 * @return true表示解锁成功；false表示解锁失败
	 */
	public boolean unlock(String lockId);

}
