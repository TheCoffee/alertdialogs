package in.ghostcode.alertdialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * Created by Coffee on 2/5/17.
 */

public class RateAppUtil {
    private final static String APP_TITLE = "MechByte";

    // Package name of the app as uploaded in the play store
    private final static String APP_NAME = "com.coffee.mechanicalbyte";

    // No of days until the app rater is shown
    private final static int DAYS_UNTIL_PROMPT = 2;
    // No of app launches until the app rater is shown
    private final static int LAUNCHES_UNTIL_PROMPT = 4;

    private Context mContext;

    public RateAppUtil(Context context) {
        mContext = context;
    }

    public void appLaunched() {
        SharedPreferences prefs = mContext.getSharedPreferences("appRater", 0);
        if (prefs.getBoolean("dontShowAgain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launchCount = prefs.getLong("launchCount", 0) + 1;
        editor.putLong("launchCount", launchCount);

        // Get date of first launch
        Long dateFirstlaunch = prefs.getLong("dateFirstLaunch", 0);
        if (dateFirstlaunch == 0) {
            dateFirstlaunch = System.currentTimeMillis();
            editor.putLong("dateFirstLaunch", dateFirstlaunch);
        }

        // Wait at least n days before opening
        if (launchCount >= LAUNCHES_UNTIL_PROMPT && 
                System.currentTimeMillis() >= dateFirstlaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
            showRateDialog(editor);
        }
        editor.apply();
    }

    public void showRateDialog(final SharedPreferences.Editor editor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Rate " + APP_TITLE);
        builder.setMessage("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putBoolean("dontShowAgain", true);
                editor.apply();
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Remind me later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Reset days and app launch count
                editor.putLong("launchCount", 0);
                editor.putLong("dateFirstLaunch", 0);
                editor.apply();
                dialogInterface.dismiss();
            }
        });

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_NAME)));
                editor.putBoolean("dontShowAgain", true);
                editor.apply();
            }
        });
        builder.show();
    }
}