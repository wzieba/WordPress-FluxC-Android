package org.wordpress.android.fluxc.utils

import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.store.WCStatsStore.StatsGranularity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val DATE_FORMAT_DEFAULT = "yyyy-MM-dd"

    /**
     * Given a [SiteModel] and a [String] compatible with [SimpleDateFormat]
     * and a {@param dateString}
     * returns a formatted date that accounts for the site's timezone setting.
     *
     */
    fun getDateTimeForSite(
        site: SiteModel,
        pattern: String,
        dateString: String?
    ): String {
        val currentDate = Date()

        if (dateString.isNullOrEmpty()) {
            return SiteUtils.getDateTimeForSite(site, pattern, currentDate)
        }

        /*
         * Since only date is provided without the time,
         * by default the time is set to the start of the day.
         *
         * This might cause timezone issues so getting the current time
         * and setting this time to the date value
         * */
        val date = getDateFromString(dateString!!)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR_OF_DAY, currentDate.hours)
        calendar.add(Calendar.MINUTE, currentDate.minutes)
        calendar.add(Calendar.SECOND, currentDate.seconds)
        return SiteUtils.getDateTimeForSite(site, pattern, calendar.time)
    }

    /**
     * returns a [Date] instance
     * based on {@param pattern} and {@param dateString}
     */
    fun getDateFromString(dateString: String): Date {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_DEFAULT, Locale.ROOT)
        return dateFormat.parse(dateString)
    }

    /**
     * returns a [String] formatted
     * based on {@param pattern} and {@param date}
     */
    fun formatDate(
        pattern: String,
        date: Date
    ): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.ROOT)
        return dateFormat.format(date)
    }

    /**
     * Given a [SimpleDateFormat] instance and
     * the [String] start date string, returns a [Calendar] instance.
     *
     * The start date time is set to 00:00:00
     *
     */
    fun getStartDateCalendar(startDate: Date): Calendar {
        val cal1 = Calendar.getInstance()
        cal1.time = startDate
        cal1.set(Calendar.HOUR_OF_DAY, 0)
        cal1.set(Calendar.MINUTE, 0)
        cal1.set(Calendar.SECOND, 0)
        cal1.set(Calendar.MILLISECOND, 0)

        return cal1
    }

    /**
     * Given a [SimpleDateFormat] instance and
     * the [String] end date string, returns a [Calendar] instance.
     *
     * The end date time is set to 23:59:59
     *
     */
    fun getEndDateCalendar(endDate: Date): Calendar {
        val cal2 = Calendar.getInstance()
        cal2.time = endDate
        cal2.set(Calendar.HOUR_OF_DAY, 23)
        cal2.set(Calendar.MINUTE, 59)
        cal2.set(Calendar.SECOND, 59)
        cal2.set(Calendar.MILLISECOND, 59)
        return cal2
    }

    /**
     * Given a [Calendar] instance for startDate and endDate,
     * returns a quantity that is calculated based on [StatsGranularity.DAYS]
     */
    fun getQuantityInDays(
        startDateCalendar: Calendar,
        endDateCalendar: Calendar
    ): Long {
        val millis1 = startDateCalendar.timeInMillis
        val millis2 = endDateCalendar.timeInMillis

        val diff = Math.abs(millis2 - millis1)
        return Math.ceil(diff / (24 * 60 * 60 * 1000).toDouble()).toLong()
    }

    /**
     * Given a [Calendar] instance for startDate and endDate,
     * returns a quantity that is calculated based on [StatsGranularity.WEEKS]
     */
    fun getQuantityInWeeks(
        startDateCalendar: Calendar,
        endDateCalendar: Calendar
    ): Long {
        /*
         * start date: if day of week is greater than 1: set to 1
         * end date: if day of week is less than 7: set to 7
         *
         * This logic is to handle half week scenarios, for instance if the
         * start date = 2019-01-25 and end date = 2019-01-28 - the difference
         * in weeks should be 2 since the dates are actually in two different weeks
         *
         * */
        if (startDateCalendar.get(Calendar.DAY_OF_WEEK) > 1) {
            startDateCalendar.set(Calendar.DAY_OF_WEEK, 1)
        }
        if (endDateCalendar.get(Calendar.DAY_OF_WEEK) < 1) {
            endDateCalendar.set(Calendar.DAY_OF_WEEK, 7)
        }

        val diffInDays = getQuantityInDays(startDateCalendar, endDateCalendar).toDouble()
        return Math.ceil(diffInDays / 7).toLong()
    }

    /**
     * Given a [Calendar] instance for startDate and endDate,
     * returns a quantity that is calculated based on [StatsGranularity.MONTHS]
     */
    fun getQuantityInMonths(
        startDateCalendar: Calendar,
        endDateCalendar: Calendar
    ): Long {
        /*
         * start date: if day of month is greater than 1: set to 1
         * end date: if day of month is less than the maximum day of month for that particular month:
         * set to maximum day of month for that particular month
         *
         * This is to handle scenarios where the start date such as if start date = 12/31/18 and end date = 1/1/19,
         * the default difference in months would be 1, but it should be 2 since these are two separate months
         * */
        if (startDateCalendar.get(Calendar.DAY_OF_MONTH) > 1) {
            startDateCalendar.set(Calendar.DAY_OF_MONTH, 1)
        }
        if (endDateCalendar.get(Calendar.DAY_OF_MONTH) < endDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            endDateCalendar.set(Calendar.DAY_OF_MONTH, endDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        var diff: Long = 0
        if (endDateCalendar.after(startDateCalendar)) {
            while (endDateCalendar.after(startDateCalendar)) {
                if (endDateCalendar.after(startDateCalendar)) {
                    diff++
                }
                startDateCalendar.add(Calendar.MONTH, 1)
            }
        }
        return Math.abs(diff)
    }

    /**
     * Given a [Calendar] instance for startDate and endDate,
     * returns a quantity that is calculated based on [StatsGranularity.YEARS]
     */
    fun getQuantityInYears(
        startDateCalendar: Calendar,
        endDateCalendar: Calendar
    ): Long {
        /*
         * start date: if day of year is greater than 1: set to 1
         * end date: if day of year is less than the maximum day of year for that particular year:
         * set to maximum day of year for that particular year
         *
         * This is to handle scenarios where the start date such as if start date = 12/31/18 and end date = 1/1/19,
         * the default difference in years would be 1, but it should be 2 since these are two separate years
         * */
        if (startDateCalendar.get(Calendar.DAY_OF_YEAR) > 1) {
            startDateCalendar.set(Calendar.DAY_OF_YEAR, 1)
        }
        if (endDateCalendar.get(Calendar.DAY_OF_YEAR) < endDateCalendar.getActualMaximum(Calendar.DAY_OF_YEAR)) {
            endDateCalendar.set(Calendar.DAY_OF_YEAR, endDateCalendar.getActualMaximum(Calendar.DAY_OF_YEAR))
        }

        var diff: Long = 0
        if (endDateCalendar.after(startDateCalendar)) {
            while (endDateCalendar.after(startDateCalendar)) {
                if (endDateCalendar.after(startDateCalendar)) {
                    diff++
                }
                startDateCalendar.add(Calendar.YEAR, 1)
            }
        }
        return Math.abs(diff)
    }
}
