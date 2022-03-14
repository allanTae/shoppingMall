package com.allan.shoppingMall.common.config.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public EhCacheManagerFactoryBean cacheManagerFactoryBean(){

        return new EhCacheManagerFactoryBean();
    }

    public CacheConfig() {
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager(){

        /**
         * shop 관련 카테고리 DTO 정보를 저장하는 캐시.
            - key : shop 카테고리 도메인의 아이디.
         */
        // cache 설정 정보.
        CacheConfiguration shopCategoryCacheConfiguration = new CacheConfiguration()
                .eternal(false)
                .timeToIdleSeconds(0)
                .timeoutMillis(28800)
                .maxEntriesLocalHeap(0)
                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
                .name("shopCategoryCaching");

        // 캐시 생성.
        Cache shopCategoryCache = new net.sf.ehcache.Cache(shopCategoryCacheConfiguration);

        // 캐시 팩토리에 생성한 생성한 캐시 추가.
        Objects.requireNonNull(cacheManagerFactoryBean().getObject()).addCache(shopCategoryCache);

        // 캐시 팩토리로 eh캐시 매니저 생성.
        return new EhCacheCacheManager(Objects.requireNonNull(cacheManagerFactoryBean().getObject()));
    }
}
