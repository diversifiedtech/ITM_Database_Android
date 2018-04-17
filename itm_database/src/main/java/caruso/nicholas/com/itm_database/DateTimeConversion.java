package caruso.nicholas.com.itm_database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Nick:3/13/2018
 * WorkOrder.
 */

public final class DateTimeConversion {
    /**
     * @param date Standard MYSQL format for dates
     * @return DATE object || null
     */
    public static Date StringToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            return format.parse(date);
        } catch (ParseException | NullPointerException ignored) {

        }
        return null;
    }

    /**
     * @param time Standard MYSQL format for dates
     * @return Time object || null
     */
    public static Date StringToTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss", Locale.US);
        try {
            return format.parse(time);
        } catch (ParseException | NullPointerException ignore) {
        }
        return null;
    }

    /**
     * @param datetime Standard MYSQL format for dates
     * @return DATETIME object || null
     */
    public static Date StringToDateTime(String datetime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        try {
            return format.parse(datetime);
        } catch (ParseException | NullPointerException ignored) {
        }
        return null;
    }

    /**
     * @param date to be formatted
     * @return Format:(January 01, 2001)
     * @throws NullPointerException is date is null
     */
    public static String DateLong(Date date) {
        String dateString;
        try {
            dateString = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault()).format(date);
        } catch (NullPointerException e) {
            dateString = null;
        }
        return dateString;
    }

    /**
     * @param date to be formatted
     * @return Format:(Jan 01, 2001)
     * @throws NullPointerException is date is null
     */
    public static String DateMedium(Date date) throws NullPointerException {
        String dateString;
        try {
            dateString = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(date);
        } catch (NullPointerException e) {
            dateString = null;
        }
        return dateString;
    }

    /**
     * @param date to be formatted
     * @return Format:(01/01/01)
     * @throws NullPointerException is date is null
     */
    public static String DateShort(Date date) throws NullPointerException {
        String dateString;
        try {
            dateString = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(date);
        } catch (NullPointerException e) {
            dateString = null;
        }
        return dateString;
    }

    /**
     * @param time to be formatted
     * @return Format:(12:01:01 AM PST)
     * @throws NullPointerException is date is null
     */
    public static String TimeLong(Date time) throws NullPointerException {
        String dateString;
        try {
            dateString = DateFormat.getTimeInstance(DateFormat.LONG, Locale.getDefault()).format(time);
        } catch (NullPointerException e) {
            dateString = null;
        }
        return dateString;
    }

    /**
     * @param time to be formatted
     * @return Format:(12:01:01 AM)
     * @throws NullPointerException is date is null
     */
    public static String TimeMedium(Date time) throws NullPointerException {
        String dateString;
        try {
            dateString = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault()).format(time);
        } catch (NullPointerException e) {
            dateString = null;
        }
        return dateString;
    }

    /**
     * @param time to be formatted
     * @return Format:(12:01 AM)
     * @throws NullPointerException is date is null
     */
    public static String TimeShort(Date time) throws NullPointerException {
        String dateString;
        try {
            dateString = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(time);
        } catch (NullPointerException e) {
            dateString = null;
        }
        return dateString;
    }

    /**
     * @param datetime to be formatted
     * @param date     size of date format: DateFormat.(LONG|MEDIUM|SHORT))
     * @param time     size of date format: DateFormat.(LONG|MEDIUM|SHORT))
     * @return desired format
     */
    public static String DateTime(Date datetime, int date, int time) {
        try {
            return DateFormat.getDateTimeInstance(date, time, Locale.getDefault()).format(datetime);
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    /**
     * @param date   to be formatted
     * @param format size of date format: DateFormat.(LONG|MEDIUM|SHORT))
     * @return desired format
     */
    public static String Date(Date date, int format) {
        try {
            return DateFormat.getDateInstance(format, Locale.getDefault()).format(date);
        } catch (NullPointerException ignored) {
        }
        return null;

    }

    /**
     * @param time   to be formatted
     * @param format size of date format: DateFormat.(LONG|MEDIUM|SHORT))
     * @return desired format
     */
    public static String Time(Date time, int format) {
        try {
            return DateFormat.getTimeInstance(format, Locale.getDefault()).format(time);
        } catch (NullPointerException ignored) {
        }
        return null;

    }

    /**
     * Gets the datetime in database format
     *
     * @param datetime a datetime
     * @return datetime in database format
     */
    public static String DatabaseDateTime(Date datetime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).format(datetime);
        } catch (NullPointerException ignored) {
        }
        return null;

    }

    /**
     * Gets the Date in database format
     *
     * @param date a date
     * @return date in database format
     */
    public static String DatabaseDate(Date date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
        } catch (NullPointerException ignored) {
        }
        return null;

    }

    /**
     * Gets the time in database format
     *
     * @param time a time
     * @return time in database format
     */
    public static String DatabaseTime(Date time) {
        try {
            return new SimpleDateFormat("hh:mm:ss", Locale.US).format(time);
        } catch (NullPointerException ignored) {
        }
        return null;

    }
}
