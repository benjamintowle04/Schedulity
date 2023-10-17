package com.example.as1;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.as1.Models.Appointment;
import com.example.as1.Models.User;
import com.example.as1.Parser.AppointmentParser;

import java.util.List;

public class ImportScheduleActivity extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_schedule);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.baseline_schedule_24);
        getSupportActionBar().setTitle("  Import Your Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button copyUrlButton = findViewById(R.id.copyUrlButton);
        copyUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://accessplus.iastate.edu/servlet/adp.A_Plus?A_Plus_action=/R480/R480.jsp&SYSTEM=R480&SUBSYS=006&SYSCODE=CS&MenuOption=7";
                copyUrlToClipboard(url);
            }
        });

        Button manualInputButton = findViewById(R.id.manualInputButton);
        manualInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String input = editText.getText().toString();
                    String[] tempTokens = input.split("\\(");
                    String[] tokens = tempTokens[1].split("\\)");
                    String appointmentString = tokens[0];

                    List<Appointment> appointmentList = AppointmentParser.parseAppointments(appointmentString, User.getUser(ImportScheduleActivity.this).appointmentList);

                    User user = User.getUser(ImportScheduleActivity.this);
                    user.addAppointmentListToList(appointmentList);
                    user.saveUser(ImportScheduleActivity.this);

//                    AppointmentController controller = new AppointmentController();
//                    String url = URL_POST_ADD_APPOINTMENT.replace("{id}", User.getUser(ImportScheduleActivity.this).getId().toString());
////TODO
//                    for (Appointment appointment : appointmentList) {
//                        controller.createAppointment(ImportScheduleActivity.this, appointment, url);
//
//                    }
//                    controller.sendAppointments(ImportScheduleActivity.this, appointmentList, url);

                    onBackPressed();
                } catch (Exception e) {
                    String message = "Error encountered: " + e.toString();
                    Toast.makeText(ImportScheduleActivity.this, message, Toast.LENGTH_LONG).show();
                    Log.d("ImportScheduleActivity", message);
                    System.out.println(message);
                }
            }
        });

        Button pasteButton = findViewById(R.id.pasteButton);
        pasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lastClipboardEntry = getLastEntryFromClipboard();
                if (lastClipboardEntry != null) {
                    editText = findViewById(R.id.showUrlET);
                    editText.setText(lastClipboardEntry);
                } else {
                    Toast.makeText(ImportScheduleActivity.this, "Clipboard is empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void copyUrlToClipboard(String url) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied URL", url);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "URL copied to clipboard", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed to copy URL", Toast.LENGTH_SHORT).show();
        }
    }

    private String getLastEntryFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null && clipboard.hasPrimaryClip()) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            CharSequence text = item.getText();
            if (text != null) {
                return text.toString();
            }
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}