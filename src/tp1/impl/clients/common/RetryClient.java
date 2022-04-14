package tp1.impl.clients.common;

import java.util.function.Supplier;
import java.util.logging.Logger;

import tp1.api.service.java.Result;
import tp1.api.service.java.Result.ErrorCode;
import util.Sleep;

/**
 * Shared client behavior.
 * 
 * Used to retry an operation in a loop.
 * 
 * @author smduarte
 *
 */
public abstract class RetryClient {
	private static Logger Log = Logger.getLogger(RetryClient.class.getName());

	protected static final int READ_TIMEOUT = 10000;
	protected static final int CONNECT_TIMEOUT = 10000;

	protected static final int RETRY_SLEEP = 100;
	protected static final int MAX_RETRIES = 3;

	// higher order function to retry forever a call until it succeeds
	// and return an object of some type T to break the loop
	protected <T> Result<T> reTry(Supplier<Result<T>> func) {
		return this.reTry(func, MAX_RETRIES);
	}

	// higher order function to retry forever a call until it succeeds
	// and return an object of some type T to break the loop
	protected <T> Result<T> reTry(Supplier<Result<T>> func, int numRetries) {
		for (int i = 0; i < numRetries; i++)
			try {
				return func.get();
			} catch (Exception x) {
				Log.finest(">>>>>>>>Exception: " + x.getMessage() + "\n");
				Sleep.ms(RETRY_SLEEP);
			}
		return Result.error(ErrorCode.TIMEOUT);
	}
}