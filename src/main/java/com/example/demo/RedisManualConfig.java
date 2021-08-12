package com.example.demo;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;

@Configuration
public class RedisManualConfig {
	@Autowired
	RedisProperties properties;
	@Value("${spring.redis.refreshTime:5}")
    private int refreshTime;

	@Bean
    public LettuceConnectionFactory redisConnectionFactory() {
		RedisProperties.Cluster clusterProperties = this.properties.getCluster();
		
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterProperties.getNodes());
        if (clusterProperties.getMaxRedirects() != null) {
        	redisClusterConfiguration.setMaxRedirects(clusterProperties.getMaxRedirects());
		}
		if (this.properties.getPassword() != null) {
			redisClusterConfiguration.setPassword(RedisPassword.of(this.properties.getPassword()));
		}
 
        //支持自适应集群拓扑刷新和静态刷新源
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions =  ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh()
//                .enableAllAdaptiveRefreshTriggers()
                .enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT, ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS)
                .refreshPeriod(Duration.ofSeconds(refreshTime))
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(10))
                .build();
 
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                .topologyRefreshOptions(clusterTopologyRefreshOptions).build();
 
        //从优先，读写分离，读从可能存在不一致，最终一致性CP
//        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
//                .readFrom(ReadFrom.SLAVE_PREFERRED)
//                .clientOptions(clusterClientOptions).build();
        LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
        .poolConfig(getPoolConfig(this.properties.getLettuce().getPool()))
        .clientOptions(clusterClientOptions).build();
 
        return new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
    }
 
 
 
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    
    @Bean
    public StringRedisTemplate stringRedisTemplateUvPv(@Qualifier("redisConnectionFactory") RedisConnectionFactory lettuceConnectionFactoryUvPv) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(lettuceConnectionFactoryUvPv);
        return stringRedisTemplate;
    }    
    private GenericObjectPoolConfig<?> getPoolConfig(Pool properties) {
		GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
		config.setMaxTotal(properties.getMaxActive());
		config.setMaxIdle(properties.getMaxIdle());
		config.setMinIdle(properties.getMinIdle());
		if (properties.getTimeBetweenEvictionRuns() != null) {
			config.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRuns().toMillis());
		}
		if (properties.getMaxWait() != null) {
			config.setMaxWaitMillis(properties.getMaxWait().toMillis());
		}
		return config;
	}
}
