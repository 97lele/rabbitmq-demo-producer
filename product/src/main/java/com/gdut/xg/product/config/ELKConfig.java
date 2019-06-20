package com.gdut.xg.product.config;

import com.google.gson.Gson;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lulu
 * @Date 2019/6/19 22:39
 */
@Configuration
public class ELKConfig {
    @Bean
    public Gson gson(){
        return new Gson();

    }

    @Bean
    public RestHighLevelClient client(){
        RestHighLevelClient client=new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost",9200,"http")
//这里如果要用client去访问其他节点，就添加进去
                )
        );
        return client;
    }

}
