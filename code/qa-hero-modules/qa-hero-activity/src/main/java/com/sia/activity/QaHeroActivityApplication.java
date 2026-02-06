package com.sia.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sia.common.security.annotation.EnableCustomConfig;
import com.sia.common.security.annotation.EnableRyFeignClients;

/**
 * 活动服务
 * 
 * @author QA Hero Team
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class QaHeroActivityApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(QaHeroActivityApplication.class, args);
    }
}
