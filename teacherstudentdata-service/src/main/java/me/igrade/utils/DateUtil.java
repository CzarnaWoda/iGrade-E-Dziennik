package me.igrade.utils;


import lombok.RequiredArgsConstructor;
import me.igrade.config.UtilsProperties;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DateUtil {

    private final UtilsProperties utilsProperties;
    public String getNowDate(){
        final Date date = new Date();

        final String pattern = utilsProperties.timeFormat();

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(date);
    }
}
