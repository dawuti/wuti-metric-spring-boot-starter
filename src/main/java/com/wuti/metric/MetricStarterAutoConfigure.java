package com.wuti.metric;


import com.codahale.metrics.MetricRegistry;
import com.netflix.hystrix.contrib.codahalemetricspublisher.HystrixCodaHaleMetricsPublisher;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricStarterAutoConfigure {

    private final Logger logger = LoggerFactory.getLogger(MetricStarterAutoConfigure.class);


    @ConditionalOnMissingBean
    @Bean
    @ConditionalOnProperty(prefix = "com.wuti.metric.hystrixMetric", value = "enabled", havingValue = "true",matchIfMissing=true)
    HystrixMetricsPublisher hystrixMetricsPublisher(MetricRegistry metricRegistry) {
        HystrixCodaHaleMetricsPublisher publisher = new HystrixCodaHaleMetricsPublisher(metricRegistry);
        HystrixPlugins.getInstance().registerMetricsPublisher(publisher);
        logger.info("HystrixMetricsPublisher加载完成");
        return publisher;
    }


    @Bean
    @ConditionalOnBean(value={com.codahale.metrics.MetricRegistry.class})
    @ConditionalOnProperty(prefix = "com.wuti.metric.undertow", value = "enabled", havingValue = "true",matchIfMissing=true)
    StartComponent startComponent(MetricRegistry metricRegistry){
        StartComponent startComponent =new StartComponent(metricRegistry);
        logger.info("StartComponent加载完成");
        return startComponent;
    }

}
