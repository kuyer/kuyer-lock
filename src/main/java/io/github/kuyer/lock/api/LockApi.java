package io.github.kuyer.lock.api;

import java.util.List;
import java.util.Map;

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
	
	/**
	 * 获取所有的锁
	 * @return ArrayList<Map<String, String>>
	 * Map对应说明
	 * lockId 锁ID
	 * lockDatetime 加锁时间 java.util.Date类型
	 * lockTimeMillis 加锁时间 单位微秒
	 * lockTimeout 超时时长，单位秒
	 * remainTimeMillis 锁持续时长，单位微秒
	 * remainTimeSeconds 锁持续时长，单位秒
	 * leaveTimeMillis 距超时时间，单位微秒
	 * leaveTimeSeconds 距超时时间，单位微秒
	 */
	public List<Map<String, Object>> getLocks();

}
