package io.github.kuyer.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.Jedis;

public class RedisFactoryBean implements FactoryBean<Jedis>, InitializingBean, DisposableBean {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Jedis jedis;
	private String host;
	private Integer port = 6379;
	
	protected void openJedisConnection() {
		this.jedis = new Jedis(this.host, this.port);
		logger.info("open jedis connection.");
	}
	
	protected void closeJedisConnection() {
		jedis.close();
		logger.info("close jedis connection.");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		openJedisConnection();
	}
	
	@Override
	public void destroy() throws Exception {
		closeJedisConnection();
	}

	@Override
	public Jedis getObject() throws Exception {
		return this.jedis;
	}

	@Override
	public Class<?> getObjectType() {
		return Jedis.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
}
