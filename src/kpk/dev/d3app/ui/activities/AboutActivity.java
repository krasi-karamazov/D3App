package kpk.dev.d3app.ui.activities;

import kpk.dev.d3app.R;
import kpk.dev.d3app.util.Utils;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class AboutActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.about_layout);
		overridePendingTransition(R.anim.activity_anim_exit, R.anim.activity_anim_enter);
		TextView title = (TextView)findViewById(R.id.about_title);
		Utils.setFont(title);
	}
}
