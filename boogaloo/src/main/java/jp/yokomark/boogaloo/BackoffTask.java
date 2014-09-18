package jp.yokomark.boogaloo;

/**
 * @author KeishinYokomaku
 */
public abstract class BackoffTask implements Runnable {
	public static final String TAG = BackoffTask.class.getSimpleName();
	private int count;

	protected void initializeCount() {
		count = 1;
	}

	protected void countUp() {
		count++;
	}

	protected int getCount() {
		return count;
	}

	protected abstract boolean shouldRetry();

}
