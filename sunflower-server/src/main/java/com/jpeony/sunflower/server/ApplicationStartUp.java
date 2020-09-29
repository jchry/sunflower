package com.jpeony.sunflower.server;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.jpeony.sunflower.common.protocol.RequestCode;
import com.jpeony.sunflower.remoting.RemotingServer;
import com.jpeony.sunflower.remoting.netty.NettyRemotingServer;
import com.jpeony.sunflower.remoting.netty.NettyServerConfig;
import com.jpeony.sunflower.server.processor.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author yihonglei
 */
@SpringBootApplication(scanBasePackages = "com.jpeony.*")
public class ApplicationStartUp implements WebMvcConfigurer, CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStartUp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        RemotingServer remotingServer = new NettyRemotingServer(nettyServerConfig);

        remotingServer.registerProcessor(RequestCode.SEND_ERROR_MONITOR_MESSAGE, new ErrorProcessor(), null);
        remotingServer.registerProcessor(RequestCode.SEND_JVM_MONITOR_MESSAGE, new JVMProcessor(), null);
        remotingServer.registerProcessor(RequestCode.SEND_HTTP_MONITOR_MESSAGE, new HttpProcessor(), null);
        remotingServer.registerProcessor(RequestCode.SEND_MYSQL_MONITOR_MESSAGE, new MySqlProcessor(), null);
        remotingServer.registerProcessor(RequestCode.SEND_REDIS_MONITOR_MESSAGE, new RedisProcessor(), null);
        remotingServer.registerProcessor(RequestCode.SEND_MONGODB_MONITOR_MESSAGE, new MongodbProcessor(), null);
        remotingServer.registerProcessor(RequestCode.SEND_ES_MONITOR_MESSAGE, new ESProcessor(), null);
        remotingServer.registerProcessor(RequestCode.SEND_CPU_MONITOR_MESSAGE, new CPUProcessor(), null);
        remotingServer.registerProcessor(RequestCode.SEND_IO_MONITOR_MESSAGE, new IOProcessor(), null);

        remotingServer.start();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue);
        List<MediaType> fastMediaTypes = Lists.newArrayList();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastMediaTypes.add(new MediaType("application", "*+json"));
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);

        converters.add(fastJsonHttpMessageConverter);
    }
}
