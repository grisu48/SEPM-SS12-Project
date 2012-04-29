package org.smartsnip.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Logging {

	public static PrintStream err = new PrintStream(new OutputStream() {

		@Override
		public void write(int arg0) throws IOException {
			// TODO Auto-generated method stub

		}
	});

	public static void printWarning(String warning) {

	}

	public static void printWarning(Throwable throwable) {

	}

	public static void printWarning(String message, Throwable throwable) {

	}

	public static void printError(String error) {

	}

	public static void printError(Throwable throwable) {

	}

	public static void printError(String message, Throwable throwable) {

	}

	public static void printInfo(String info) {

	}

}
