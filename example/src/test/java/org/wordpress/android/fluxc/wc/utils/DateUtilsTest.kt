package org.wordpress.android.fluxc.wc.utils

import org.junit.Test
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.test.assertEquals

class DateUtilsTest {
    companion object {
        private const val DATE_FORMAT_DAY = "yyyy-MM-dd"
    }

    @Test
    fun testGetDateFromDateString() {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_DAY, Locale.ROOT)

        val d1 = "2018-01-25"
        val date1 = DateUtils.getDateFromString(d1)
        assertEquals(dateFormat.parse(d1), date1)

        val d2 = "2018-01-28"
        val date2 = DateUtils.getDateFromString(d2)
        assertEquals(dateFormat.parse(d2), date2)

        val d3 = "2018-01-01"
        val date3 = DateUtils.getDateFromString(d3)
        assertEquals(dateFormat.parse(d3), date3)
    }

    @Test
    fun testFormatDateFromString() {
        val d1 = "2019-02-12"

        val site = SiteModel().apply { id = 1 }
        val date1 = DateUtils.getDateTimeForSite(site, DATE_FORMAT_DAY, d1)
        assertEquals(d1, date1)

        site.timezone = "12"
        val date2 = DateUtils.getDateTimeForSite(site, DATE_FORMAT_DAY, d1)
        assertEquals(d1, date2)

        site.timezone = "-12"
        val date3 = DateUtils.getDateTimeForSite(site, DATE_FORMAT_DAY, d1)
        assertEquals("2019-02-11", date3)

        site.timezone = "0"
        val date4 = DateUtils.getDateTimeForSite(site, DATE_FORMAT_DAY, d1)
        assertEquals(d1, date4)

        site.timezone = ""
        val date5 = DateUtils.getDateTimeForSite(site, DATE_FORMAT_DAY, d1)
        assertEquals(d1, date5)
    }

    @Test
    fun testGetQuantityInDays() {
        val startDate = DateUtils.getDateFromString("2018-01-25")
        val endDate = DateUtils.getDateFromString("2018-01-28")
        val startDateCalendar = DateUtils.getStartDateCalendar(startDate)
        val endDateCalendar = DateUtils.getEndDateCalendar(endDate)
        val quantity = DateUtils.getQuantityInDays(startDateCalendar, endDateCalendar)
        assertEquals(4, quantity)

        val startDate2 = DateUtils.getDateFromString("2018-01-25")
        val endDate2 = DateUtils.getDateFromString("2018-01-25")
        val startDateCalendar2 = DateUtils.getStartDateCalendar(startDate2)
        val endDateCalendar2 = DateUtils.getEndDateCalendar(endDate2)
        val quantity2 = DateUtils.getQuantityInDays(startDateCalendar2, endDateCalendar2)
        assertEquals(1, quantity2)

        val startDate3 = DateUtils.getDateFromString("2018-01-01")
        val endDate3 = DateUtils.getDateFromString("2018-01-31")
        val startDateCalendar3 = DateUtils.getStartDateCalendar(startDate3)
        val endDateCalendar3 = DateUtils.getEndDateCalendar(endDate3)
        val quantity3 = DateUtils.getQuantityInDays(startDateCalendar3, endDateCalendar3)
        assertEquals(31, quantity3)
    }

    @Test
    fun testGetQuantityInWeeks() {
        val startDate = DateUtils.getDateFromString("2019-01-13")
        val endDate = DateUtils.getDateFromString("2019-01-20")
        val startDateCalendar = DateUtils.getStartDateCalendar(startDate)
        val endDateCalendar = DateUtils.getEndDateCalendar(endDate)
        val quantity = DateUtils.getQuantityInWeeks(startDateCalendar, endDateCalendar)
        assertEquals(2, quantity)

        val startDate2 = DateUtils.getDateFromString("2018-12-01")
        val endDate2 = DateUtils.getDateFromString("2018-12-31")
        val startDateCalendar2 = DateUtils.getStartDateCalendar(startDate2)
        val endDateCalendar2 = DateUtils.getEndDateCalendar(endDate2)
        val quantity2 = DateUtils.getQuantityInWeeks(startDateCalendar2, endDateCalendar2)
        assertEquals(6, quantity2)

        val startDate3 = DateUtils.getDateFromString("2018-10-22")
        val endDate3 = DateUtils.getDateFromString("2018-10-22")
        val startDateCalendar3 = DateUtils.getStartDateCalendar(startDate3)
        val endDateCalendar3 = DateUtils.getEndDateCalendar(endDate3)
        val quantity3 = DateUtils.getQuantityInWeeks(startDateCalendar3, endDateCalendar3)
        assertEquals(1, quantity3)
    }

    @Test
    fun testGetQuantityInMonths() {
        val startDate = DateUtils.getDateFromString("2018-12-13")
        val endDate = DateUtils.getDateFromString("2019-01-20")
        val startDateCalendar = DateUtils.getStartDateCalendar(startDate)
        val endDateCalendar = DateUtils.getEndDateCalendar(endDate)
        val quantity = DateUtils.getQuantityInMonths(startDateCalendar, endDateCalendar)
        assertEquals(2, quantity)

        val startDate2 = DateUtils.getDateFromString("2018-12-01")
        val endDate2 = DateUtils.getDateFromString("2018-12-31")
        val startDateCalendar2 = DateUtils.getStartDateCalendar(startDate2)
        val endDateCalendar2 = DateUtils.getEndDateCalendar(endDate2)
        val quantity2 = DateUtils.getQuantityInMonths(startDateCalendar2, endDateCalendar2)
        assertEquals(1, quantity2)

        val startDate3 = DateUtils.getDateFromString("2017-10-22")
        val endDate3 = DateUtils.getDateFromString("2018-10-22")
        val startDateCalendar3 = DateUtils.getStartDateCalendar(startDate3)
        val endDateCalendar3 = DateUtils.getEndDateCalendar(endDate3)
        val quantity3 = DateUtils.getQuantityInMonths(startDateCalendar3, endDateCalendar3)
        assertEquals(13, quantity3)
    }

    @Test
    fun testGetQuantityInYears() {
        val startDate = DateUtils.getDateFromString("2018-12-13")
        val endDate = DateUtils.getDateFromString("2019-01-20")
        val startDateCalendar = DateUtils.getStartDateCalendar(startDate)
        val endDateCalendar = DateUtils.getEndDateCalendar(endDate)
        val quantity = DateUtils.getQuantityInYears(startDateCalendar, endDateCalendar)
        assertEquals(2, quantity)

        val startDate2 = DateUtils.getDateFromString("2018-12-01")
        val endDate2 = DateUtils.getDateFromString("2018-12-31")
        val startDateCalendar2 = DateUtils.getStartDateCalendar(startDate2)
        val endDateCalendar2 = DateUtils.getEndDateCalendar(endDate2)
        val quantity2 = DateUtils.getQuantityInYears(startDateCalendar2, endDateCalendar2)
        assertEquals(1, quantity2)

        val startDate3 = DateUtils.getDateFromString("2016-10-22")
        val endDate3 = DateUtils.getDateFromString("2018-10-22")
        val startDateCalendar3 = DateUtils.getStartDateCalendar(startDate3)
        val endDateCalendar3 = DateUtils.getEndDateCalendar(endDate3)
        val quantity3 = DateUtils.getQuantityInYears(startDateCalendar3, endDateCalendar3)
        assertEquals(3, quantity3)
    }
}
