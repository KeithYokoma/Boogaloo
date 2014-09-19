package jp.yokomark.boogaloo;

import android.os.Handler;
import android.support.annotation.NonNull;

/**
 * @author KeithYokoma
 */
@SuppressWarnings("unused") // public APIs
public class ExponentialBackoffConfigurator extends BackoffConfigurator {
	public static final String TAG = ExponentialBackoffConfigurator.class.getSimpleName();
	private long retryInterval;
	private long maxInterval;
	private int multiplier;

	public ExponentialBackoffConfigurator(Boogaloo boogaloo) {
		super(boogaloo);
	}

	public void interval(long retryInterval) {
		this.retryInterval = retryInterval;
	}

	public void multiplier(int multiplier) {
		this.multiplier = multiplier;
	}

	public void until(long max) {
		maxInterval = max;
	}

	@NonNull
	@Override
	protected Runnable createTaskRunner() {
		return new Runnable() {
			@Override
			public void run() {
				BackoffTask task = getTask();
				task.run();
				retryInterval *= Math.pow(multiplier, task.getCount() - 1);
				if (retryInterval <= maxInterval && task.shouldRetry()) {
					task.countUp();
					Handler handler = getBoogaloo().getHandler();
					handler.postDelayed(this, retryInterval);
				} else {
					getBoogaloo().setCurrentConfiguration(null);
				}
			}
		};
	}
}
