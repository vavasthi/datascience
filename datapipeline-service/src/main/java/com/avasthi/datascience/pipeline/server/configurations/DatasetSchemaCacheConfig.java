package com.avasthi.datascience.pipeline.server.configurations;

import com.avasthi.datascience.caching.pojos.KeyPrefixForCache;
import com.avasthi.datascience.pipeline.server.entities.DatasetDefinitionSchemaEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;

@Configuration
public class DatasetSchemaCacheConfig {

        private
        @Value("${redis.nodes:localhost}")
        String redisHost;
        @Value("${redis.database:1}")
        private int redisDatabase;
        private
        @Value("${redis.password:#{null}}")
        String redisPassword;
        @Value("${redis.pool.maxIdle:5}")
        private int maxIdle;
        private
        @Value("${redis.pool.minIdle:1}")
        int minIdle;
        private
        @Value("${redis.pool.maxRedirects:3}")
        int maxRedirects;
        private
        @Value("${redis.pool.maxTotal:20}")
        int maxTotal;
        private
        @Value("${redis.pool.maxWaitMillis:3000}")
        int maxWaitMillis;

        JedisConnectionFactory jedisSchedulerConnectionFactory() {

            String[] hosts = redisHost.split(",");
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxIdle(maxIdle);
            poolConfig.setMinIdle(minIdle);
            poolConfig.setMaxWaitMillis(maxWaitMillis);
            poolConfig.setMaxTotal(maxTotal);

            if (hosts.length == 1) {
                RedisStandaloneConfiguration standaloneConfiguration
                        = new RedisStandaloneConfiguration();
                standaloneConfiguration.setDatabase(redisDatabase);
                String[] hostPort = redisHost.split(":");
                if (hostPort.length > 1) {
                    standaloneConfiguration.setPort(Integer.valueOf(hostPort[1]));
                }
                standaloneConfiguration.setHostName(hostPort[0]);
                if (redisPassword != null) {

                    standaloneConfiguration.setPassword(RedisPassword.of(redisPassword));
                }

                JedisConnectionFactory factory = new JedisConnectionFactory(standaloneConfiguration);
                factory.afterPropertiesSet();
                return factory;
            }
            else {

                RedisClusterConfiguration configuration = new RedisClusterConfiguration(Arrays.asList(hosts));
                if (redisPassword != null || !redisPassword.isEmpty()) {
                    configuration.setPassword(RedisPassword.of(redisPassword));
                }
                configuration.setMaxRedirects(maxRedirects);
                JedisConnectionFactory factory = new JedisConnectionFactory(configuration, poolConfig);
                factory.afterPropertiesSet();
                return factory;
            }
        }

        public RedisTemplate<KeyPrefixForCache, DatasetDefinitionSchemaEntity> redisTemplate() {
            RedisTemplate<KeyPrefixForCache, DatasetDefinitionSchemaEntity> redisTemplate
                    = new RedisTemplate<KeyPrefixForCache, DatasetDefinitionSchemaEntity>();
            redisTemplate.setConnectionFactory(jedisSchedulerConnectionFactory());
            redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
            redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
            redisTemplate.setEnableTransactionSupport(true);
            redisTemplate.afterPropertiesSet();
            return redisTemplate;
        }
}
