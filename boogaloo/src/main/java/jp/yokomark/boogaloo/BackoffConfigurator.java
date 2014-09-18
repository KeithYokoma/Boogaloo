package jp.yokomark.boogaloo;

import android.os.Handler;
import android.support.annotation.NonNull;

/**
 * @author KeishinYokomaku
 */
@SuppressWarnings("unused") // public APIs
public abstract class BackoffConfigurator {
	public static final String TAG = BackoffConfigurator.class.getSimpleName();
	private final Boogaloo boogaloo;
	private Runnable taskRunner;
	private BackoffTask task;

	public BackoffConfigurator(Boogaloo boogaloo) {
		this.boogaloo = boogaloo;
	}

	public void execute(@NonNull final BackoffTask task) {
		this.task = task;
		this.task.initializeCount();
		taskRunner = createTaskRunner();
		Handler handler = getBoogaloo().getHandler();
		handler.post(taskRunner);
		boogaloo.setCurrentConfiguration(this);
	}

	void clearPrevious() {
		if (taskRunner == null) {
			return;
		}
		Handler handler = getBoogaloo().getHandler();
		handler.removeCallbacks(taskRunner);
	}

	@NonNull
	protected Boogaloo getBoogaloo() {
		return boogaloo;
	}

	@NonNull
	protected BackoffTask getTask() {
		return task;
	}

	@NonNull
	protected abstract Runnable createTaskRunner();
}
