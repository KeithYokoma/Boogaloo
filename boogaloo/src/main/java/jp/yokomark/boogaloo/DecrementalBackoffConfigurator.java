package jp.yokomark.boogaloo;

import android.os.Handler;
import android.support.annotation.NonNull;

/**
 * @author KeishinYokomaku
 */
@SuppressWarnings("unused") // public APIs
public class DecrementalBackoffConfigurator extends BackoffConfigurator {
	public static final String TAG = DecrementalBackoffConfigurator.class.getSimpleName();
	private long retryInterval;
	private long minInterval;
	private long subtraction;

	public DecrementalBackoffConfigurator(Boogaloo boogaloo) {
		super(boogaloo);
	}

	@NonNull
	public DecrementalBackoffConfigurator interval(long retryInterval) {
		this.retryInterval = retryInterval;
		return this;
	}

	@NonNull
	public DecrementalBackoffConfigurator subtraction(int subtraction) {
		this.subtraction = subtraction;
		return this;
	}

	@NonNull
	public DecrementalBackoffConfigurator until(long minInterval) {
		this.minInterval = minInterval;
		return this;
	}

	@NonNull
	@Override
	protected Runnable createTaskRunner() {
		return new Runnable() {
			@Override
			public void run() {
				BackoffTask task = getTask();
				task.run();
				long nextInterval = retryInterval - (subtraction * (task.getCount()));
				if (nextInterval >= minInterval && task.shouldRetry()) {
					task.countUp();
					Handler handler = getBoogaloo().getHandler();
					handler.postDelayed(this, nextInterval);
				} else {
					getBoogaloo().setCurrentConfiguration(null);
				}
			}
		};
	}
}
