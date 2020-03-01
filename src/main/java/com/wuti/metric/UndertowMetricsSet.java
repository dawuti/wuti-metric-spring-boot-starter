package com.wuti.metric;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UndertowMetricsSet implements MetricSet {
    private final Logger logger = LoggerFactory.getLogger(UndertowMetricsSet.class);


    private MBeanServer mbeanServer;

    public UndertowMetricsSet(MBeanServer mbeanServer) {
        this.mbeanServer = mbeanServer;
    }

    @Override
    public Map<String, Metric> getMetrics() {
        final Map<String, Metric> gauges = new HashMap<String, Metric>();

        logger.info("UndertowPublishMetrics收集");
        try {
            Set<ObjectName> objectNames = mbeanServer.queryNames(new ObjectName("org.xnio:type=Xnio,provider=\"nio\",worker=*"), null);
            logger.info("UndertowPublishMetrics收集，{}",objectNames);

            if (objectNames != null ) {

                for (ObjectName objectName : objectNames) {
                    String  providerName = (String) mbeanServer.getAttribute(objectName, "Name");

                    int coreWorkerPoolSize = (Integer) mbeanServer.getAttribute(objectName, "CoreWorkerPoolSize");
                    int maxWorkerPoolSize = (Integer) mbeanServer.getAttribute(objectName, "MaxWorkerPoolSize");
                    int workerPoolSize = (Integer) mbeanServer.getAttribute(objectName, "WorkerPoolSize");
                    int busyWorkerThreadCount = (Integer) mbeanServer.getAttribute(objectName, "BusyWorkerThreadCount");
                    int ioThreadCount = (Integer) mbeanServer.getAttribute(objectName, "IoThreadCount");
                    int workerQueueSize = (Integer) mbeanServer.getAttribute(objectName, "WorkerQueueSize");

                    gauges.put("work."+providerName+".thread.coreWorkerPoolSize", new Gauge<Long>() {
                        @Override
                        public Long getValue() {
                            return Long.valueOf(coreWorkerPoolSize);
                        }
                    });
                    gauges.put("work."+providerName+".thread.maxWorkerPoolSize", new Gauge<Long>() {
                        @Override
                        public Long getValue() {
                            return Long.valueOf(maxWorkerPoolSize);
                        }
                    });
                    gauges.put("work."+providerName+".thread.workerPoolSize", new Gauge<Long>() {
                        @Override
                        public Long getValue() {
                            return Long.valueOf(workerPoolSize);
                        }
                    });
                    gauges.put("work."+providerName+".thread.busyWorkerThreadCount", new Gauge<Long>() {
                        @Override
                        public Long getValue() {
                            return Long.valueOf(busyWorkerThreadCount);
                        }
                    });
                    gauges.put("work."+providerName+".thread.ioThreadCount", new Gauge<Long>() {
                        @Override
                        public Long getValue() {
                            return Long.valueOf(ioThreadCount);
                        }
                    });
                    gauges.put("work."+providerName+".thread.workerQueueSize", new Gauge<Long>() {
                        @Override
                        public Long getValue() {
                            return Long.valueOf(workerQueueSize);
                        }
                    });

                }
            } else {
                logger.warn("undertow thread pool monitor failed");
            }

        } catch (Exception e) {
            logger.error("undertow thread pool monitor exception", e);
        }
        return Collections.unmodifiableMap(gauges);

    }
}

