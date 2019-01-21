package br.com.anteros.security.store.mongo.exception;

public class NoSQLStoreException extends RuntimeException {

	public NoSQLStoreException() {
	}

	public NoSQLStoreException(String message) {
		super(message);
	}

	public NoSQLStoreException(Throwable cause) {
		super(cause);
	}

	public NoSQLStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSQLStoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
