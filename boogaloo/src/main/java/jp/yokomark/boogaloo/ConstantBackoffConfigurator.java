package jp.yokomark.boogaloo;

import android.os.Handler;
import android.support.annotation.NonNull;

/**
 * @author KeishinYokomaku
 */
public class ConstantBackoffConfigurator extends BackoffConfigurator {
	private long retryInterval;
	private long maxInterval;

	public ConstantBackoffConfigurator(Boogaloo boogaloo) {
		super(boogaloo);
	}

	@NonNull
	public ConstantBackoffConfigurator interval(long retryInterval) {
		this.retryInterval = retryInterval;
		return this;
	}

	@NonNull
	public ConstantBackoffConfigurator until(long maxInterval) {
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
				long nextInterval = retryInterval + (retryInterval * (task.getCount()));
				if (nextInterval <= maxInterval && task.shouldRetry()) {
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
