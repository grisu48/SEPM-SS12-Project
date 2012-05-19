package org.smartsnip.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {

	public static PrintStream err = new PrintStream(new OutputStream() {

		@Override
		public void write(int b) throws IOException {
			System.err.write(b);
		}
	});
	public static PrintStream out = new PrintStream(new OutputStream() {
		
		@Override
		public void write(int b) throws IOException {
			System.out.write(b);
		}
	});

	public static void printWarning(String warning) {
		if (warning == null || warning.isEmpty()) return;
		out.println(getTimeHeader() + "[WARN] " + warning);
	}

	public static void printWarning(Throwable throwable) {
		if (throwable == null) return;
		printWarning("Exception " + throwable.getMessage());
	}

	public static void printWarning(String message, Throwable throwable) {
		if (message == null || message.isEmpty()) {
			printWarning(throwable);
			return;
		}
		if (throwable == null) {
			printWarning(message);
			return;
		}
		printWarning(message + "(Exception: " + throwable.getMessage() + ")");

	}

	public static void printError(String message) {
		if (message == null || message.isEmpty()) return;
		err.println(getTimeHeader() + "[ERR] " + message);
	}

	public static void printError(Throwable throwable) {
		if (throwable == null) return;
		printError("Exception " + throwable.getMessage());
	}

	public static void printError(String message, Throwable throwable) {
		if (message == null || message.isEmpty()) {
			printError(throwable);
			return;
		}
		if (throwable == null) {
			printError(message);
			return;
		}
		printError(message + "(Exception: " + throwable.getMessage() + ")");

	}

	public static void printInfo(String info) {
		if (info == null || info.isEmpty()) return;
		
		out.println(getTimeHeader() + "[INFO] " + info);
	}

	
	/**
	 * @return Gets a formatted time header for the messages
	 */
	private static String getTimeHeader() {
		SimpleDateFormat simpleDateFormat =
			        new SimpleDateFormat("yyyy.MM.dd-hh:mm:ss");
		return "[" + simpleDateFormat.format(new Date()) + "] ";
	}
}
