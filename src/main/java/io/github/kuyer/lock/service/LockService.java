package io.github.kuyer.lock.service;

import io.github.kuyer.lock.api.LockApi;

import org.springframework.stereotype.Service;

/** 锁服务 **/
@Service
public class LockService implements LockApi {

	@Override
	public boolean lock(String lockId, Integer lockTimeout) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unlock(String lockId) {
		// TODO Auto-generated method stub
		return false;
	}

}
