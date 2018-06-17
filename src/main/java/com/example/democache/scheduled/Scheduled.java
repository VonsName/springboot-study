package com.example.democache.scheduled;

import org.springframework.stereotype.Service;

/**
 * @author ASUS
 */
@Service
public class Scheduled {
    /**
     * second(秒0-59) minute(分0-59), hour(时0-23), day of month(每月哪天1-31), month(月份1-12)
     * 	 day of week(周几0-7或者SUN-SAT).
     * 	 "0 * * * * MON-FRI" ：周一到周五的任意时刻的整秒执行(每分钟的整秒执行任务)
     * 	 允许的特殊字符【,枚举 -区间 *任意 /步长 ?日/星期 L最后
     * 	               W工作日 C和calendar计算过后的值 #第几个 >4#2 第二个星期四】
     */
    //@org.springframework.scheduling.annotation.Scheduled(cron = "0 * * * * SUN-SAT")

    /**
     * 0,1,2,3,4 * * * * SUN-SAT 整秒以及1，2，3，4秒的时候执行
     */
    //@org.springframework.scheduling.annotation.Scheduled(cron = "0,1,2,3,4 * * * * SUN-SAT")

    /**
     * 0-4 * * * * SUN-SAT 0-4秒执行
     */
    //@org.springframework.scheduling.annotation.Scheduled(cron = "0-4 * * * * SUN-SAT")

    /**
     *  每分钟的第五秒执行
     */
    //@org.springframework.scheduling.annotation.Scheduled(cron = "5 */1 * * * SUN-SAT")

    //每分钟的每四秒执行一次
    //@org.springframework.scheduling.annotation.Scheduled(cron = "*/4 */1 * * * SUN-SAT")

    //每分钟执行一次
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 */1 * * * SUN-SAT")
    public void test(){
        System.out.println("定时任务");
    }
}
