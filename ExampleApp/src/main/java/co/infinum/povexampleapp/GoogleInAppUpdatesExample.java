package co.infinum.povexampleapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.infinum.queenofversions.GoogleInAppUpdateCallback;
import com.infinum.queenofversions.GoogleInAppUpdateFlexibleHandler;
import com.infinum.queenofversions.UpdaterStateCallback;

import co.infinum.princeofversions.Loader;
import co.infinum.princeofversions.NetworkLoader;
import co.infinum.princeofversions.PrinceOfVersions;

public class GoogleInAppUpdatesExample extends AppCompatActivity implements UpdaterStateCallback {

    private static final String TAG = "GoogleInAppUpdates";
    private final int REQUEST_CODE = 420;

    private GoogleInAppUpdateCallback googleInAppUpdateCallback;
    private PrinceOfVersions princeOfVersions;
    private Loader loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_inapp_updates);

        initUI();

        princeOfVersions = new PrinceOfVersions.Builder().build(this);
        googleInAppUpdateCallback = new GoogleInAppUpdateCallback(REQUEST_CODE, this, this, BuildConfig.VERSION_CODE);
        loader = new NetworkLoader("http://pastebin.com/raw/QFGjJrLP");
    }

    private void initUI() {
        Button checkUpdatesButton = findViewById(R.id.checkUpdatesButton);
        checkUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckUpdatesClick();
            }
        });
    }

    private void onCheckUpdatesClick() {
        princeOfVersions.checkForUpdates(loader, googleInAppUpdateCallback);
    }

    @Override
    public void onNoUpdate() {
        Toast.makeText(this, "No updates!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloading() {
        Toast.makeText(this, "Downloading update...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInstalling() {
        Toast.makeText(this, "Installing update...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequiresUI() {
        Toast.makeText(this, "Requires UI!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloaded(GoogleInAppUpdateFlexibleHandler handler) {
        Toast.makeText(this, "Downloaded!", Toast.LENGTH_SHORT).show();
        handler.completeUpdate();
    }

    @Override
    public void onCanceled() {
        Toast.makeText(this, "Canceled update!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInstalled() {
        Toast.makeText(this, "Installed update!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPending() {
        Toast.makeText(this, "Update pending...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnknown() {
        Toast.makeText(this, "Unknown status!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(Exception exception) {
        Toast.makeText(this, "Failed updated! Check log!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Exception:", exception.fillInStackTrace());
    }
}
