package com.sia.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sia.common.security.annotation.EnableCustomConfig;
import com.sia.common.security.annotation.EnableRyFeignClients;

/**
 * 钱包服务
 * 
 * @author QA Hero Team
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class QaHeroWalletApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(QaHeroWalletApplication.class, args);
    }
}
