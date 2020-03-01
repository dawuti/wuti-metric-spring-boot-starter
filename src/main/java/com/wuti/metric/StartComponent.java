package com.wuti.metric;


import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.lang.management.ManagementFactory;


public class StartComponent implements ApplicationRunner
{

    private final Logger logger = LoggerFactory.getLogger(StartComponent.class);

    private MetricRegistry metricRegistry;

    public StartComponent(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public void run(ApplicationArguments args)
        throws Exception
    {
        logger.info("项目启动执行方法start");

        // undertow
        metricRegistry.register("undertow", new UndertowMetricsSet(ManagementFactory.getPlatformMBeanServer()));

       logger.info("项目启动执行方法end");
    }
}

