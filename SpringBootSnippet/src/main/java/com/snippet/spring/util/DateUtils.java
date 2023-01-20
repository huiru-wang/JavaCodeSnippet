public class DateUtils{
  public static final String DEFAULT_FMT = "yyyy-MM-dd HH:mm:ss";
     public static String getUtcDateStr(@NonNull String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return "";
        }
        Date localDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_FMT);
        try {
            localDate = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("illegal date format");
        }
        long time = localDate.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        Date utcDate = new Date(calendar.getTimeInMillis());
        return simpleDateFormat.format(utcDate);
    } 
  
}
