package in.ghostcode.alertdialogs;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RateAppUtil mRateAppUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRateAppUtil = new RateAppUtil(this);
        mRateAppUtil.appLaunched();
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.simple_dialog:
                showSimpleDialog();
                break;
            case R.id.about_dialog:
                showAboutDialog();
                break;
            case R.id.feedback_dialog:
                showFeedbackDialog();
                break;
            case R.id.app_rater_dialog:
                // The call to the showRateDialog method is for illustrative purposes only.
                // It is enough to call appLaunched() method in the MainActivity
                SharedPreferences prefs = getSharedPreferences("appRater", 0);
                SharedPreferences.Editor editor = prefs.edit();
                mRateAppUtil.showRateDialog(editor);
                break;
        }
    }

    private void showSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do some action
            }
        });
        builder.show();
    }

    // Alert Dialog with custom view
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About the app");
        builder.setView(R.layout.about_dialog);
        builder.show();
    }

    // Alert Dialog with custom view and EditText inputs
    private void showFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.feedback_dialog, null);
        final TextView feedbackName = (TextView) dialogView.findViewById(R.id.feedback_name);
        final TextView feedbackEmail = (TextView) dialogView.findViewById(R.id.feedback_email);
        final TextView feedbackContent = (TextView) dialogView.findViewById(R.id.feedback_content);

        builder.setView(dialogView);
        builder.setTitle("Please share your feedback");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = feedbackName.getText().toString();
                String email = feedbackEmail.getText().toString();
                String content = feedbackContent.getText().toString();

                Toast.makeText(MainActivity.this, "Name: " + name + "\nEmail: " + email + "\nFeedback: " + content, Toast.LENGTH_SHORT).show();

            }
        });

        builder.show();

    }

}
