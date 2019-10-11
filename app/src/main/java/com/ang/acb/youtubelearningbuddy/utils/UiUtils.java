package com.ang.acb.youtubelearningbuddy.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;


public class UiUtils {

    // See: https://stackoverflow.com/questions/13600802/android-convert-dp-to-float
    public static float dipToPixels(Context context, float dipValue){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  dipValue, metrics);
    }

    public static LocalDate formatPublishedAtDate(String dateString) {
        // Date is an RFC 3339 formatted date-time value (1970-01-01T00:00:00Z).
        // See: https://stackoverflow.com/questions/17479366/parse-rfc-3339-from-string-to-java-util-date-using-joda
        return ISODateTimeFormat.dateTime().parseLocalDate(dateString);
    }
}
