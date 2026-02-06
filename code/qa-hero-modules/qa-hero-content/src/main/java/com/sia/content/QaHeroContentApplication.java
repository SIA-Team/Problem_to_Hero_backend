package com.sia.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sia.common.security.annotation.EnableCustomConfig;
import com.sia.common.security.annotation.EnableRyFeignClients;

/**
 * 内容模块
 * 
 * @author sia
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class QaHeroContentApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(QaHeroContentApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  内容模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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
