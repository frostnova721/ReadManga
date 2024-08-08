package app.ice.readmanga.utils

import android.content.Context
import android.widget.Toast

data class MonthNumberToMonthNameResult(
    val full: String,
    val short: String,
);

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

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
