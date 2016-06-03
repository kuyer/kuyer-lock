package io.github.kuyer.lock.service;

import io.github.kuyer.lock.api.LockApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

/** 锁服务 **/
@Service
public class LockService implements LockApi {
	
	/** 锁键前缀 **/
	private static final String LOCK_KEY_PREFIX = "kuyer-lock-";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Jedis jedis;

	@Override
	public boolean lock(String lockId, Integer lockTimeout) {
		if(StringUtils.isBlank(lockId)) {
			return false;
		}
		String key = LOCK_KEY_PREFIX + lockId.trim();
		try {
			if(jedis.exists(key)) {
				return false;
			}
			String value = String.valueOf(System.currentTimeMillis());
			if(null!=lockTimeout && lockTimeout>=1) {
				value += "-"+lockTimeout.toString();
			}
			if(jedis.setnx(key, value)>=1) {
				if(null!=lockTimeout && lockTimeout>=1) {
					jedis.expire(key, lockTimeout);
					logger.info("lock {} success. lock timeout: {}", lockId, lockTimeout);
				} else {
					logger.info("lock {} success.", lockId);
				}
				return true;
			}
		} catch (Exception e) {
			logger.error("lock {} error. {}", lockId.trim(), e);
		}
		return false;
	}

	@Override
	public boolean unlock(String lockId) {
		if(StringUtils.isBlank(lockId)) {
			return false;
		}
		String key = LOCK_KEY_PREFIX + lockId.trim();
		try {
			if(jedis.del(key)>=1l) {
				logger.info("unlock {} success.", lockId);
				return true;
			}
		} catch (Exception e) {
			logger.error("unlock {} error. {}", lockId, e);
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getLocks() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Set<String> keys = jedis.keys(LOCK_KEY_PREFIX+"*");
			if(null!=keys && keys.size()>=1) {
				for(String key : keys) {
					String val = jedis.get(key);
					if(StringUtils.isNotBlank(val)) {
						key = key.replaceFirst(LOCK_KEY_PREFIX, "");
						Long lockTimeMillis = null;
						Integer lockTimeout = null;
						if(val.indexOf("-")>=0) {
							String[] vals = val.split("-");
							lockTimeMillis = Long.parseLong(vals[0].trim());
							lockTimeout = Integer.parseInt(vals[1]);
						} else {
							lockTimeMillis = Long.parseLong(val.trim());
						}
						Long remainTimeMillis = System.currentTimeMillis() - lockTimeMillis;
						Long remainTimeSeconds = remainTimeMillis/1000l;
						Date lockDatetime = new Date(lockTimeMillis);
						Long leaveTimeMillis = null;
						Long leaveTimeSeconds = null;
						if(null != lockTimeout) {
							leaveTimeMillis = lockTimeout.longValue()*1000l - remainTimeMillis;
							leaveTimeSeconds = leaveTimeMillis/1000l;
						}
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("lockId", key);//锁ID
						map.put("lockDatetime", lockDatetime);//加锁时间 java.util.Date类型
						map.put("lockTimeMillis", lockTimeMillis);//加锁时间 单位微秒
						map.put("lockTimeout", lockTimeout);//超时时长，单位秒
						map.put("remainTimeMillis", remainTimeMillis);//锁持续时长，单位微秒
						map.put("remainTimeSeconds", remainTimeSeconds);//锁持续时长，单位秒
						map.put("leaveTimeMillis", leaveTimeMillis);//距超时时间，单位微秒
						map.put("leaveTimeSeconds", leaveTimeSeconds);//距超时时间，单位微秒
						list.add(map);
					}
				}
			}
		} catch (Exception e) {
			logger.error("getLocks error.", e);
		}
		return list;
	}

}
