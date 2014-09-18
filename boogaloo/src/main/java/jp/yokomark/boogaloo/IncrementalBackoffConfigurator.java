package jp.yokomark.boogaloo;

import android.os.Handler;
import android.support.annotation.NonNull;

/**
 * @author KeishinYokomaku
 */
@SuppressWarnings("unused") // public APIs
public class IncrementalBackoffConfigurator extends BackoffConfigurator {
	public static final String TAG = IncrementalBackoffConfigurator.class.getSimpleName();
	private long retryInterval;
	private long maxInterval;
	private long addition;

	public IncrementalBackoffConfigurator(Boogaloo boogaloo) {
		super(boogaloo);
	}

	@NonNull
	public IncrementalBackoffConfigurator interval(long retryInterval) {
		this.retryInterval = retryInterval;
		return this;
	}

	@NonNull
	public IncrementalBackoffConfigurator addition(int addition) {
		this.addition = addition;
		return this;
	}

	@NonNull
	public IncrementalBackoffConfigurator until(long maxInterval) {
		this.maxInterval = maxInterval;
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
				long nextInterval = retryInterval + (addition * (task.getCount()));
				if (nextInterval <= maxInterval && task.shouldRetry()) {
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
