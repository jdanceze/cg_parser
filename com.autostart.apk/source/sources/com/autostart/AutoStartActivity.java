package com.autostart;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.util.LruCache;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class AutoStartActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "*******AutoStart*********  ";
    private ArrayList<PInfo> appList;
    private Button bt;
    private Button btDiscoLight;
    private CheckBox cb;
    private CheckBox cbGoTOHome;
    private CheckBox cb_showAll;
    private EditText etDelay;
    private EditText etDelayBetween;
    private LruCache<String, Drawable> imageCache;
    private IconifiedTextListAdapter ita;
    private LinearLayout llAppSelect;
    private LinearLayout llApps;
    private ListView lv;
    private List<PInfo> newPackageList;
    private List<PInfo> newPackageList_all;
    private List<ApplicationInfo> packages;
    private PackageManager pm;
    private ProgressBar progressbar;
    private OnResumeTask resumeTask;
    private float scale;
    private ScrollView svAppSettings;
    private ToggleButton tb;
    private TextView textView1;
    private TextView textView2;
    private TextView tvDelay;
    private TextView tvDelayBetween;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ActivityManager am = (ActivityManager) getSystemService("activity");
        int memoryClass = am.getMemoryClass() * 1024 * 1024;
        this.imageCache = new LruCache<>(memoryClass / 2);
        this.appList = new ArrayList<>();
        this.lv = (ListView) findViewById(R.id.listView1);
        this.tb = (ToggleButton) findViewById(R.id.toggleButton1);
        this.bt = (Button) findViewById(R.id.btSelect);
        this.btDiscoLight = (Button) findViewById(R.id.btDiscoLight);
        this.llApps = (LinearLayout) findViewById(R.id.llApps);
        this.textView1 = (TextView) findViewById(R.id.textView1);
        this.textView2 = (TextView) findViewById(R.id.textView2);
        this.progressbar = (ProgressBar) findViewById(R.id.progress_bar);
        this.tvDelay = (TextView) findViewById(R.id.tvDelay);
        this.tvDelayBetween = (TextView) findViewById(R.id.tvDelayBetween);
        this.cb = (CheckBox) findViewById(R.id.checkBox1);
        this.etDelay = (EditText) findViewById(R.id.etDelay);
        this.etDelayBetween = (EditText) findViewById(R.id.etDelayBetween);
        this.cb_showAll = (CheckBox) findViewById(R.id.checkBox2);
        this.llAppSelect = (LinearLayout) findViewById(R.id.llappSelect);
        this.svAppSettings = (ScrollView) findViewById(R.id.svAppSettings);
        this.cbGoTOHome = (CheckBox) findViewById(R.id.cbJumpToHome);
        this.scale = getResources().getDisplayMetrics().density;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService("notification");
        mNotificationManager.cancel(3461);
        this.bt.setOnClickListener(new View.OnClickListener() { // from class: com.autostart.AutoStartActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                AutoStartActivity.this.showMainScreen(false);
            }
        });
        this.btDiscoLight.setOnClickListener(new View.OnClickListener() { // from class: com.autostart.AutoStartActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("market://details?id=com.discolight"));
                AutoStartActivity.this.startActivity(intent);
            }
        });
        this.cb_showAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.autostart.AutoStartActivity.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AutoStartActivity.this.ita = new IconifiedTextListAdapter(AutoStartActivity.this.getApplicationContext(), AutoStartActivity.this.newPackageList_all);
                } else {
                    AutoStartActivity.this.ita = new IconifiedTextListAdapter(AutoStartActivity.this.getApplicationContext(), AutoStartActivity.this.newPackageList);
                }
                AutoStartActivity.this.lv.setAdapter((ListAdapter) AutoStartActivity.this.ita);
            }
        });
        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.autostart.AutoStartActivity.4
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                AutoStartActivity.this.showMainScreen(true);
                PInfo pi = (PInfo) AutoStartActivity.this.ita.getItem(pos);
                AutoStartActivity.this.appList.add(pi);
                AutoStartActivity.this.addToAppList(pi);
            }
        });
    }

    protected void addToAppList(PInfo pi) {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(0);
        TextView tv = new TextView(this);
        tv.setPadding(3, 6, 3, 6);
        tv.setTextColor(-11493136);
        tv.setTextSize(22.0f);
        tv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        tv.setText(pi.appname);
        tv.setTag(pi.appname);
        tv.setOnClickListener(this);
        LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(-2, -2);
        paramsText.gravity = 19;
        paramsText.weight = 1.0f;
        ll.addView(tv, paramsText);
        TextView mTextDelete = new TextView(this);
        mTextDelete.setText("X");
        LinearLayout.LayoutParams paramsTextDel = new LinearLayout.LayoutParams(-2, -2);
        paramsTextDel.gravity = 21;
        ll.addView(mTextDelete, paramsTextDel);
        mTextDelete.setTextSize(22.0f);
        tv.setPadding(3, 6, 3, 6);
        mTextDelete.setTextColor(SupportMenu.CATEGORY_MASK);
        mTextDelete.setTag(pi.appname);
        mTextDelete.setOnClickListener(this);
        this.llApps.addView(ll);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable resize(Drawable image) {
        float scale = getResources().getDisplayMetrics().density;
        int width = (int) ((85.0f * scale) + 0.5f);
        int height = (int) ((85.0f * scale) + 0.5f);
        try {
            Bitmap d = ((BitmapDrawable) image.getCurrent()).getBitmap();
            Bitmap bitmapOrig = Bitmap.createScaledBitmap(d, width, height, false);
            return new BitmapDrawable(bitmapOrig);
        } catch (Exception e) {
            return null;
        }
    }

    @Override // android.app.Activity
    public void onResume() {
        this.progressbar.setVisibility(0);
        this.newPackageList = new ArrayList();
        this.newPackageList_all = new ArrayList();
        this.cb_showAll.setChecked(false);
        if (this.llAppSelect.getVisibility() == 0) {
            showMainScreen(true);
        }
        this.bt.setEnabled(false);
        this.pm = getPackageManager();
        this.packages = this.pm.getInstalledApplications(128);
        this.resumeTask = new OnResumeTask(this, null);
        this.resumeTask.execute(new Void[0]);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("autostart", 0);
        String packageName = prefs.getString("package", "");
        String className = prefs.getString("class", "");
        this.cb.setChecked(prefs.getBoolean("noti", true));
        this.cbGoTOHome.setChecked(prefs.getBoolean("gotohome", true));
        boolean enabled = prefs.getBoolean("enabled", false);
        this.etDelay.setText(Integer.toString(prefs.getInt("startdelay", 0)));
        this.etDelayBetween.setText(Integer.toString(prefs.getInt("inbetweendelay", 3)));
        if (!packageName.equalsIgnoreCase("") && !className.equalsIgnoreCase("")) {
            if (enabled) {
                this.tb.setChecked(true);
            } else {
                this.tb.setChecked(false);
            }
            String[] splitPackages = packageName.split(";;");
            String[] splitClasses = className.split(";;");
            for (int i = 0; i < splitPackages.length; i++) {
                PInfo pi = new PInfo();
                pi.appname = splitClasses[i];
                pi.pname = splitPackages[i];
                this.appList.add(pi);
                addToAppList(pi);
            }
        }
        super.onResume();
    }

    @Override // android.app.Activity
    public void onPause() {
        String packageName = "";
        String className = "";
        this.lv.setAdapter((ListAdapter) new IconifiedTextListAdapter(this, new ArrayList()));
        for (int i = 0; i < this.appList.size(); i++) {
            if (i == 0) {
                packageName = this.appList.get(i).pname;
                className = this.appList.get(i).appname;
            } else {
                packageName = String.valueOf(packageName) + ";;" + this.appList.get(i).pname;
                className = String.valueOf(className) + ";;" + this.appList.get(i).appname;
            }
        }
        SharedPreferences prefs = getSharedPreferences("autostart", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("package", packageName);
        edit.putString("class", className);
        edit.putBoolean("enabled", this.tb.isChecked());
        edit.putBoolean("noti", this.cb.isChecked());
        edit.putBoolean("gotohome", this.cbGoTOHome.isChecked());
        edit.putInt("iteration", 0);
        try {
            edit.putInt("startdelay", Integer.parseInt(this.etDelay.getText().toString()));
            edit.putInt("inbetweendelay", Integer.parseInt(this.etDelayBetween.getText().toString()));
        } catch (Exception e) {
            edit.putInt("startdelay", 0);
            edit.putInt("inbetweendelay", 3);
        }
        edit.commit();
        if (this.resumeTask != null && !this.resumeTask.myCancel) {
            this.resumeTask.myCancel();
        }
        this.appList.clear();
        this.llApps.removeAllViews();
        super.onPause();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getRepeatCount() == 0 && this.llAppSelect.getVisibility() == 0) {
            showMainScreen(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMainScreen(boolean b) {
        if (b) {
            this.svAppSettings.setVisibility(0);
            this.llAppSelect.setVisibility(8);
            return;
        }
        this.svAppSettings.setVisibility(8);
        this.llAppSelect.setVisibility(0);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 65536);
        return list.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSystemPackage(ApplicationInfo aapplicationInfo) {
        return (aapplicationInfo.flags & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class IconifiedTextListAdapter extends BaseAdapter {
        private Context mContext;
        private List<PInfo> mItems;

        public IconifiedTextListAdapter(Context context, List<PInfo> items) {
            this.mContext = context;
            this.mItems = items;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            try {
                return this.mItems.size();
            } catch (Exception e) {
                return 0;
            }
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return this.mItems.get(position);
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                return new IconifiedTextView(this.mContext, this.mItems.get(position));
            }
            PInfo pi = this.mItems.get(position);
            IconifiedTextView btv = (IconifiedTextView) convertView;
            if (btv.getText().equalsIgnoreCase(pi.appname)) {
                return btv;
            }
            btv.setText(pi.appname);
            Drawable d = (Drawable) AutoStartActivity.this.imageCache.get(pi.pname);
            if (d == null) {
                try {
                    AutoStartActivity.this.imageCache.put(pi.pname, AutoStartActivity.this.resize(pi.packageInfo.loadIcon(AutoStartActivity.this.getPackageManager())));
                    btv.setIcon((Drawable) AutoStartActivity.this.imageCache.get(pi.pname));
                    return btv;
                } catch (Exception e) {
                    return btv;
                }
            }
            btv.setIcon(d);
            return btv;
        }
    }

    /* loaded from: classes.dex */
    private class IconifiedTextView extends LinearLayout {
        private ImageView mIcon;
        private TextView mText;

        public IconifiedTextView(Context context, PInfo appInfo) {
            super(context);
            setOrientation(0);
            this.mIcon = new ImageView(context);
            this.mIcon.setImageDrawable((Drawable) AutoStartActivity.this.imageCache.get(appInfo.pname));
            this.mIcon.setPadding(3, 10, 5, 10);
            int IconSize = (int) ((72.0f * AutoStartActivity.this.scale) + 0.5f);
            addView(this.mIcon, new LinearLayout.LayoutParams(IconSize, IconSize));
            this.mText = new TextView(context);
            this.mText.setPadding(1, 8, 0, 10);
            this.mText.setText(appInfo.appname);
            LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(-2, -2);
            paramsText.gravity = 16;
            addView(this.mText, paramsText);
            this.mText.setTextSize(22.0f);
            this.mText.setTextColor(-1);
        }

        public void setText(String words) {
            this.mText.setText(words);
        }

        public String getText() {
            return this.mText.getText().toString();
        }

        public void setIcon(Drawable bullet) {
            this.mIcon.setImageDrawable(bullet);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class PInfo implements Comparable<PInfo> {
        private ApplicationInfo packageInfo;
        private String appname = "";
        private String pname = "";

        public PInfo() {
        }

        @Override // java.lang.Comparable
        public int compareTo(PInfo p) {
            return this.appname.compareToIgnoreCase(p.appname);
        }
    }

    /* loaded from: classes.dex */
    private class OnResumeTask extends AsyncTask<Void, Void, Void> {
        private boolean myCancel;

        private OnResumeTask() {
            this.myCancel = false;
        }

        /* synthetic */ OnResumeTask(AutoStartActivity autoStartActivity, OnResumeTask onResumeTask) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... arg0) {
            for (ApplicationInfo packageInfo : AutoStartActivity.this.packages) {
                if (!this.myCancel) {
                    if (AutoStartActivity.this.pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                        PInfo pi = new PInfo();
                        pi.appname = packageInfo.loadLabel(AutoStartActivity.this.getPackageManager()).toString();
                        pi.pname = packageInfo.packageName;
                        pi.packageInfo = packageInfo;
                        if (!AutoStartActivity.this.isSystemPackage(packageInfo)) {
                            AutoStartActivity.this.newPackageList.add(pi);
                        }
                        AutoStartActivity.this.newPackageList_all.add(pi);
                    }
                    Collections.sort(AutoStartActivity.this.newPackageList);
                    Collections.sort(AutoStartActivity.this.newPackageList_all);
                } else {
                    return null;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void result) {
            if (!this.myCancel) {
                AutoStartActivity.this.ita = new IconifiedTextListAdapter(AutoStartActivity.this, AutoStartActivity.this.newPackageList);
                AutoStartActivity.this.lv.setAdapter((ListAdapter) AutoStartActivity.this.ita);
            }
            this.myCancel = true;
            AutoStartActivity.this.bt.setEnabled(true);
            AutoStartActivity.this.progressbar.setVisibility(4);
        }

        protected void myCancel() {
            this.myCancel = true;
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(final View v) {
        try {
            String title = (String) ((TextView) v).getTag();
            String message = "Remove " + title + " from autostart list?";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title).setMessage(message).setNegativeButton("Yes", new DialogInterface.OnClickListener() { // from class: com.autostart.AutoStartActivity.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    try {
                        AutoStartActivity.this.llApps.removeView((ViewGroup) v.getParent());
                        TextView textView = (TextView) v;
                        int i = 0;
                        while (i < AutoStartActivity.this.appList.size() && !((PInfo) AutoStartActivity.this.appList.get(i)).appname.equalsIgnoreCase((String) ((TextView) v).getTag())) {
                            i++;
                        }
                        if (i < AutoStartActivity.this.appList.size()) {
                            AutoStartActivity.this.appList.remove(i);
                        }
                    } catch (Exception e) {
                    }
                }
            }).setPositiveButton("No", new DialogInterface.OnClickListener() { // from class: com.autostart.AutoStartActivity.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
        }
    }
}
