package pk.hotfixdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends Activity {

    private CheckBox box;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        box = (CheckBox) findViewById(R.id.box);
        button = (Button) findViewById(R.id.button);

        box.setChecked(Config.isEnable());
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Config.setEnable(isChecked);
                Toast.makeText(MainActivity.this, "配置已生效，请重启App测试", Toast.LENGTH_LONG).show();

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Bug code", Toast.LENGTH_LONG).show();
            }
        });
    }
}
