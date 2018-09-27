package io.github.jingweiwang.androidtimessquare.sample1;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CalendarPickerView dialogView;
    private AlertDialog theDialog;
    private Button dialog;
    private Calendar range;
    private Date today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = findViewById(R.id.button_dialog);

        range = Calendar.getInstance();
        range.add(Calendar.MONTH, 2);
//
//        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        today = new Date();
//        calendar.init(today, range.getTime())
//                .withSelectedDate(today)
//                .inMode(CalendarPickerView.SelectionMode.SINGLE);


        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "I'm a dialog!";
                showCalendarInDialog(title, R.layout.dialog);
                dialogView.init(today, range.getTime())
                        .withSelectedDate(today)
                        .inMode(CalendarPickerView.SelectionMode.SINGLE);
            }
        });

    }

    private void showCalendarInDialog(String title, int layoutResId) {
        dialogView = (CalendarPickerView) getLayoutInflater().inflate(layoutResId, null, false);
        theDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(dialogView)
//                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Selected time in millis: " + dialogView.getSelectedDate().getTime());
                        String toast = "Selected: " + dialogView.getSelectedDate().getTime();
                        Toast.makeText(MainActivity.this, toast, LENGTH_SHORT).show();
                    }
                })
                .create();
        theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Log.d(TAG, "onShow: fix the dimens!");
                dialogView.fixDialogDimens();
            }
        });
        theDialog.show();
    }
}
