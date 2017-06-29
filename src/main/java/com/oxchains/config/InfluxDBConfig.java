package com.oxchains.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oxchains.service.influxDB.InfluxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by song on 2017/6/26.
 */
@Configuration
@ComponentScan
public class InfluxDBConfig {

    @Autowired
    private MonitoringParams monitoringParams;

    @Bean
    public InfluxService influxDBFactory() {
        return new InfluxService(
                monitoringParams.getUrlInfluxDB(),
                monitoringParams.getUsernameInfluxDB(),
                monitoringParams.getPasswordInfluxDB(),
                monitoringParams.getDbName()
        );
    }

    @Bean
    public ObjectMapper jsonObjectMapper(){
        ObjectMapper m = new ObjectMapper();
        m.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return m;
    }

//    @Bean
//    public ResultAdapter resultAdapter() {
//        return new ResultAdapter() {bu'dia
//            @Override
//            public Optional<Point> apply(Object o, InvokeMonitoring invokeMonitoring) {
//                return Optional.empty();
//            }
//        };
//    }
}

