package win.lioil.tantanhelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        if (!AccessibilityUtil.isSettingOpen(AutoClickService.class, this)) {
            AccessibilityUtil.jumpToSetting(this);
            Toast.makeText(this, "请打开“探探自动右滑”服务", Toast.LENGTH_LONG).show();
        } else
            AccessibilityUtil.jumpToTanTan(this);
    }
}
