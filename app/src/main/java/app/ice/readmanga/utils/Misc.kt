package app.ice.readmanga.utils

import org.json.JSONArray
import org.json.JSONObject

data class MonthNumberToMonthNameResult(
    val full: String,
    val short: String,
);

fun MonthNumberToName(monthNumber: Int): MonthNumberToMonthNameResult {
    val monthName = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
    );
     return MonthNumberToMonthNameResult(full = monthName[monthNumber-1], short = monthName[monthNumber-1].substring(0,3))
}
