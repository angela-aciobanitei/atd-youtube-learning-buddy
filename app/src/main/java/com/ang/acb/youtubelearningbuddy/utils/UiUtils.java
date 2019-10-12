package com.ang.acb.youtubelearningbuddy.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsViewModel;

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

    public static void createNewTopicDialog(MainActivity activity, TopicsViewModel topicsViewModel) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        View dialogView = activity.getLayoutInflater()
                .inflate(R.layout.create_new_topic_dialog, null);
        dialogBuilder.setView(dialogView);

        // Setup dialog buttons
        final EditText editText = dialogView.findViewById(R.id.dialog_edit_text);
        dialogBuilder.setPositiveButton(R.string.dialog_pos_btn, (dialog, whichButton) -> {
            String input = editText.getText().toString();
            if (input.trim().length() != 0) topicsViewModel.createTopic(input);
            else dialog.dismiss();
        });

        dialogBuilder.setNegativeButton(R.string.dialog_neg_btn, (dialog, whichButton) ->
                dialog.cancel());

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        // Customize dialog buttons
        Button posBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        posBtn.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent));
        posBtn.setTextColor(ContextCompat.getColor(activity,R.color.colorAccent));
        posBtn.setPadding(16, 0, 16, 0);
        Button negBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negBtn.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent));
        negBtn.setTextColor(ContextCompat.getColor(activity,R.color.colorAccent));
        negBtn.setPadding(16, 0, 16, 0);
    }
}
