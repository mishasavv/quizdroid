package edu.washington.mikhail3.quizdroid;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.preference.*;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Filter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ArrayList<Topic> topics;
    private DownloadManager manager;
    QuizApp quizApp;
    private int D_A = 1;

    /*BroadcastReceiver ar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

            Toast.makeText(MainActivity.this, preferences.getString("down_url", ""), Toast.LENGTH_LONG).show();
        }
    };*/
    BroadcastReceiver r = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String a = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(a)) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (id != 0) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(id);
                    Cursor cursor = manager.query(query);
                    if (cursor.moveToFirst()) {
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        switch (status) {
                            case DownloadManager.STATUS_RUNNING:
                            case DownloadManager.STATUS_PENDING:
                            case DownloadManager.STATUS_PAUSED:
                            break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                Log.i("Download", "Successs");
                                try {
                                    QuizApp quizApp = (QuizApp) getApplication();
                                    FileInputStream in = new FileInputStream(manager.openDownloadedFile(id).getFileDescriptor());
                                    String json = SingleApp.loadJSON(in);
                                    SingleApp.write(json, quizApp.getApplicationContext());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "File Error--Check settings to set correct download location", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case DownloadManager.STATUS_FAILED:
                                Log.i("MainActivity", "Fail");
                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                dialog.setTitle("Question Download Failure")
                                        .setMessage("Retry Download? \"No\" closes app.")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                Log.v("Download", "Fail, close");
                                                System.exit(0);
                                            }
                                        })
                                        .create()
                                        .show();
                                break;
                        }
                    }
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.pref_downloads, false);
        quizApp = (QuizApp) getApplication();
        topics = quizApp.getQuiz();
        final ArrayList<String> quizzes = new ArrayList<String>();
        int length = topics.size();
        for (int i = 0; i < length; i++) {
            Topic topic = topics.get(i);
            quizzes.add(topic.getTitle());
        }
        ListView quizList = (ListView) findViewById(R.id.quizList);
        quizList.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_activated_1, quizzes));
        quizList.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent next = new Intent(MainActivity.this, QuizActivity.class);
                next.putExtra("quiz", position);
                next.putExtra("quizData", topics);
                startActivity(next);
                finish();
            }
        });
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        IntentFilter f = new IntentFilter();
        f.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(r, f); //this
        alarmStartStop(this, true);
    }

    public void alarmStartStop(Context context, boolean turnedOn) {
        Intent amIntent = new Intent();
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        amIntent.setAction("edu.washington.mikhail3.quizdroid");
        PendingIntent pi = PendingIntent.getBroadcast(context, D_A, amIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        registerReceiver(ar, new IntentFilter("edu.washington.mikhail3.quizdroid"));
        if (turnedOn) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            int interval = (Integer.parseInt(prefs.getString("down_freq", null)) * 20 * 1000);
            //int interval = 10000;
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pi);
        } else {
            manager.cancel(pi);
            pi.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(MainActivity.this, prefActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        unregisterReceiver(ar);
        unregisterReceiver(r);
    }

    @SuppressWarnings("deprecation")
    private static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public static void setSettings(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.putInt(
                    context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0);
        } else {
            Settings.Global.putInt(
                    context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0);
        }
    }
    BroadcastReceiver ar = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context c, Intent i) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo net = cm.getActiveNetworkInfo();
            boolean connectivity = net != null && net.isConnectedOrConnecting();
            if (connectivity) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String url = pref.getString("down_url", null);
                Log.i("Download", "downloading " + url);
                //Toast.makeText(MainActivity.this, "Downloading from source: " + url, Toast.LENGTH_SHORT).show();
                manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                try {
                    manager.enqueue(new DownloadManager.Request(Uri.parse(url)));
                } catch (IllegalArgumentException e) {
                    Toast.makeText(MainActivity.this, "Invalid URL, Edit in Settings", Toast.LENGTH_LONG).show();
                }
            } else {
                    if (isAirplaneModeOn(c)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Airplane Mode On")
                                .setMessage("Cannot update questions. Disable Airplane Mode?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                            startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));
//                                        } else {
//                                            Settings.Global.putString(getContentResolver(), "airplane_mode_on", "1");
//                                        }

                                            startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));

                                        //startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));
                                        //Settings.Global.putString(getContentResolver(), "airplane_mode_on", "1");
                                        Log.i("Airplane", "yes");
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Log.i("Airplane", "no");
                                    }
                                })
                                .create()
                                .show();
                        Log.i("ConnectivityCheck", "airplane");
                } else {
                    Toast.makeText(MainActivity.this, "Cannot update questions. Connectivity unavailable, try again later.", Toast.LENGTH_LONG).show();
                    Log.i("ConnectivityCheck", "no signal");
                }
            }
        }
    };

}