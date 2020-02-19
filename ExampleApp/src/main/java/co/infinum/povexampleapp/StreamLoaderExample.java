package co.infinum.povexampleapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Map;

import javax.annotation.Nonnull;

import co.infinum.princeofversions.Loader;
import co.infinum.princeofversions.PrinceOfVersions;
import co.infinum.princeofversions.PrinceOfVersionsCancelable;
import co.infinum.princeofversions.Result;
import co.infinum.princeofversions.StreamLoader;
import co.infinum.princeofversions.UpdaterCallback;

public class StreamLoaderExample extends AppCompatActivity {

    private final UpdaterCallback defaultCallback = new UpdaterCallback() {
        @Override
        public void onNewUpdate(@NonNull int version, boolean isMandatory, @NonNull Map<String, String> metadata) {
            toastIt(
                getString(
                    R.string.update_available_msg,
                    getString(isMandatory ? R.string.mandatory : R.string.not_mandatory),
                    version
                ),
                Toast.LENGTH_SHORT
            );
        }

        @Override
        public void onNoUpdate(@NonNull Map<String, String> metadata) {
            toastIt(getString(R.string.no_update_available), Toast.LENGTH_SHORT);
        }

        @Override
        public void onError(@Nonnull Throwable throwable) {
            throwable.printStackTrace();
            toastIt(String.format(getString(R.string.update_exception), throwable.getMessage()), Toast.LENGTH_SHORT);
        }
    };

    private final Handler handler = new Handler(Looper.getMainLooper());

    private PrinceOfVersions updater;

    private PrinceOfVersionsCancelable cancelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_usage);
        initUI();

        /*  create new instance of updater */
        updater = new PrinceOfVersions(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onCancelClick();
    }

    private void initUI() {
        Button btnCheck = findViewById(R.id.btnCheck);
        Button btnCancelTest = findViewById(R.id.btnCancelTest);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnCheckSync = findViewById(R.id.btnCheckSync);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckClick();
            }
        });
        btnCancelTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelTestClick();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClick();
            }
        });
        btnCheckSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckSyncClick();
            }
        });
    }

    private void onCheckClick() {
        /*  call check for updates for start checking and remember return value if you need cancel option    */
        PrinceOfVersionsCancelable cancelable = updater
            .checkForUpdates(new StreamLoader(getResources().openRawResource(R.raw.update)), defaultCallback);
        replaceCancelable(cancelable);
    }

    private void onCheckSyncClick() {
        /*  call check for updates for start checking and remember return value if you need cancel option    */
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Result result = updater.checkForUpdates(new StreamLoader(getResources().openRawResource(R.raw.update)));
                    toastItOnMainThread("Update check finished with status " + result.getStatus() + " and version " + result.getVersion(),
                        Toast.LENGTH_LONG);
                } catch (Throwable throwable) {
                    toastItOnMainThread("Error occurred " + throwable.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, "Example thread");
        thread.start();
    }

    private void onCancelTestClick() {
        /*  same call as few lines higher, but using another loader, this one is very slow loader just to demonstrate cancel
        functionality. */
        PrinceOfVersionsCancelable cancelable = updater
            .checkForUpdates(createSlowLoader(new StreamLoader(getResources().openRawResource(R.raw.update))), defaultCallback);
        replaceCancelable(cancelable);
    }

    private void onCancelClick() {
        /*  cancel current checking request, checking if context is not consumed yet is not necessary   */
        if (cancelable != null) {
            cancelable.cancel();
        }
    }

    private void replaceCancelable(PrinceOfVersionsCancelable call) {
        /*  started new checking, kill current one if not dead and remember new context */
        if (this.cancelable != null) {
            this.cancelable.cancel();
        }
        this.cancelable = call;
    }

    private void toastIt(final String message, final int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    private void toastItOnMainThread(final String message, final int duration) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                toastIt(message, duration);
            }
        });
    }

    private Loader createSlowLoader(final Loader loader) {
        return new Loader() {
            @Override
            public String load() throws Throwable {
                Thread.sleep(2000);
                return loader.load();
            }
        };
    }
}
