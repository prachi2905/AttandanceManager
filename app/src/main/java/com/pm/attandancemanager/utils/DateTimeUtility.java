package com.pm.attandancemanager.utils;



public class DateTimeUtility {

  /*
    public static String getCurrentDate()
    {
            return new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT).format(new Date());
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat(Constants.TIME_STAMP_FORMAT).format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTimeStamp() {
        String pattern = Constants.SERVER_DATE_FORMAT; //2017-07-25T11:47:04.404Z
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        return simpleDateFormat.format(new Date());
    }

    public static String getFormatDDMMMYYYY(String date) {
        SimpleDateFormat sdfmt2 = new SimpleDateFormat(Constants.SERVER_DATE_FORMAT);
        sdfmt2.setTimeZone(TimeZone.getTimeZone("IST"));

        String dateX = "";
        try {
            Date dDate = sdfmt2.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            dateX = simpleDateFormat.format(dDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateX;
    }


    public static String getChatTime(long date) {
        SimpleDateFormat sdfmt2 = new SimpleDateFormat("h:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return sdfmt2.format(calendar.getTime());
    }

    public static String getChatTimeDDMMMYYYY(long date) {
        SimpleDateFormat sdfmt2 = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return sdfmt2.format(calendar.getTime());
    }*/
}
