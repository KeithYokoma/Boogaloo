package jp.yokomark.boogaloo;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author KeithYokoma
 */
@SuppressWarnings("unused") // public APIs
public class Boogaloo {
	public static final String TAG = Boogaloo.class.getSimpleName();
	private static volatile Boogaloo singleton;
	private final Handler handler;
	private BackoffConfigurator current;

	Boogaloo(@NonNull Handler handler) {
		this.handler = handler;
	}

	public static void clear() {
		if (singleton == null) {
			Log.i(TAG, "no current configuration found");
			return;
		}
		if (singleton.current == null) {
			Log.i(TAG, "no current configuration found");
			return;
		}
		singleton.current.clearPrevious();
	}

	public static Boogaloo setup() {
		return setup(new Handler(Looper.getMainLooper()));
	}

	public static Boogaloo setup(@NonNull Handler handler) {
		if (singleton == null) {
			synchronized (Boogaloo.class) {
				if (singleton == null) { // double check to ensure the field is null.
					singleton = new Boogaloo(handler);
				}
			}
		}
		return singleton;
	}

	public IncrementalBackoffConfigurator incremental() {
		return new IncrementalBackoffConfigurator(this);
	}

	public ConstantBackoffConfigurator constant() {
		return new ConstantBackoffConfigurator(this);
	}

	public DecrementalBackoffConfigurator decremental() {
		return new DecrementalBackoffConfigurator(this);
	}

	public ExponentialBackoffConfigurator exponential() {
		return new ExponentialBackoffConfigurator(this);
	}

	@NonNull
	Handler getHandler() {
		return handler;
	}

	void setCurrentConfiguration(BackoffConfigurator current) {
		this.current = current;
	}
}
