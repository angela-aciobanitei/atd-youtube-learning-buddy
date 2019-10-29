package com.ang.acb.youtubelearningbuddy.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsViewModel;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;


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

    public static String dateToString(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return formatter.format(date);
        } else {
            return "";
        }
    }

    public static void showNewTopicDialog(MainActivity activity, TopicsViewModel topicsViewModel) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        View dialogView = activity.getLayoutInflater().inflate(R.layout.create_topic_dialog, null);
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();

        final EditText editText = dialogView.findViewById(R.id.dialog_new_edit_text);
        Button saveButton = dialogView.findViewById(R.id.dialog_new_save_btn);
        saveButton.setOnClickListener(view -> {
            String input = editText.getText().toString();
            if (input.trim().length() != 0) topicsViewModel.createTopic(input);
            dialog.dismiss();
        });

        Button cancelButton = dialogView.findViewById(R.id.dialog_new_cancel_btn);
        cancelButton.setOnClickListener(view -> dialog.cancel());

        dialog.show();
    }
}
