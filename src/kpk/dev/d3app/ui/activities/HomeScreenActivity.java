package kpk.dev.d3app.ui.activities;

import kpk.dev.d3app.R;
import kpk.dev.d3app.util.Utils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeScreenActivity extends AbstractActivity{
	private Button mProfilesButton;
	private Button mServerCheckerButton;
	private Button mSettingsButton;
	private Button mAboutButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComponents();	
	}
	
	@Override
	protected void initComponents() {
		Utils.setFont((ViewGroup)getWindow().getDecorView());
		mProfilesButton = (Button)findViewById(R.id.add_profile_button);
		mServerCheckerButton = (Button)findViewById(R.id.server_checker_button);
		mSettingsButton = (Button)findViewById(R.id.settings_button);
		mAboutButton = (Button)findViewById(R.id.about_button);
		
		mProfilesButton.setOnClickListener(mainScreenClickListener);
		mServerCheckerButton.setOnClickListener(mainScreenClickListener);
		mSettingsButton.setOnClickListener(mainScreenClickListener);
		mAboutButton.setOnClickListener(mainScreenClickListener);
	}
	
	private View.OnClickListener mainScreenClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.add_profile_button:
					startActivity(new Intent(HomeScreenActivity.this, ProfilesListActivity.class));
					break;
			
				case R.id.server_checker_button:
					startActivity(new Intent(HomeScreenActivity.this, ServerCheckerActivty.class));
					break;
					
				case R.id.settings_button:
					break;
					
				case R.id.about_button:
					break;
			}
			//showAccountDialog();
		}
	};
}
