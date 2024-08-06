package Insuleng.Insuleng_Backend.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class TimeUtil {
    public static String getTimeLine(LocalDateTime writingTime){

        LocalDateTime now = LocalDateTime.now();

        if(ChronoUnit.SECONDS.between(writingTime, now) <60){
            return ChronoUnit.SECONDS.between(writingTime, now) + "초 전";
        }else if(ChronoUnit.MINUTES.between(writingTime, now) < 60){
            return ChronoUnit.MINUTES.between(writingTime, now) +"분 전";
        }else if(ChronoUnit.HOURS.between(writingTime, now) < 24){
            return ChronoUnit.HOURS.between(writingTime, now) + "시간 전";
        }else if(ChronoUnit.YEARS.between(writingTime, now) == 0){
            return writingTime.getMonth() + "/" + writingTime.getDayOfMonth();
        }else{
            return ChronoUnit.YEARS.between(writingTime, now) + "년 전";
        }

    }

}
