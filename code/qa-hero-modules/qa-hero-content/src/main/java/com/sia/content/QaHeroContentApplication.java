package com.sia.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sia.common.security.annotation.EnableCustomConfig;
import com.sia.common.security.annotation.EnableRyFeignClients;

/**
 * 内容服务
 * 
 * @author QA Hero Team
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class QaHeroContentApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(QaHeroContentApplication.class, args);
    }
}
