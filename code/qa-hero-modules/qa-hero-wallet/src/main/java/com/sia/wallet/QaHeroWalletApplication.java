package com.sia.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sia.common.security.annotation.EnableCustomConfig;
import com.sia.common.security.annotation.EnableRyFeignClients;

/**
 * 钱包模块
 * 
 * @author sia
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class QaHeroWalletApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(QaHeroWalletApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  钱包模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
