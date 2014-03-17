package com.etwin.service.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
	public static void main(String[] args) {
    String str="20110214";
    String str1="20110225";
    SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
    Calendar start = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    try {
        start.setTime(format.parse(str));
        end.setTime(format.parse(str1));
    while(start.before(end)){
        System.out.println(format.format(start.getTime()));
        start.add(Calendar.DAY_OF_MONTH,1);
    } } catch (ParseException e) {
      e.printStackTrace();
  }
}
}
