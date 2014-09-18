package jp.yokomark.boogaloo.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import jp.yokomark.boogaloo.BackoffTask;
import jp.yokomark.boogaloo.Boogaloo;

/**
 * @author KeithYokoma
 */
public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onStartClick(View view) {
		Boogaloo.clear();
		Boogaloo.setup().constant().interval(1000).until(10000).execute(new BackoffTask() {
			@Override
			protected boolean shouldRetry() {
				return true;
			}

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), getCount() + ": Hey!", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
