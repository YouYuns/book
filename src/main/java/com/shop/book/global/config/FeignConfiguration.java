package com.shop.book.global.config;


import com.shop.book.global.error.FeignClientExceptionErrorDecoder;
import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableFeignClients(basePackages = "com.shop.book") //todo 패키지명 수정
@Import(FeignConfiguration.class)
public class FeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    //ERROR DECODER로 등록
    @Bean
    public ErrorDecoder errorDecoder(){
        return new FeignClientExceptionErrorDecoder();
    }

    //Retryer설정
    @Bean
    public Retryer retryer(){
        //Parameter 1. 실행주기 2. 실행주기 최대 3. 최대 몇번 재시도

        return new Retryer.Default(1000, 2000, 3);
    }

}
