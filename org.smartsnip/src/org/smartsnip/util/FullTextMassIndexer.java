/**
 * File: FullTextMassIndexer.java
 * Date: 09.06.2012
 */
package org.smartsnip.util;

import org.smartsnip.persistence.hibernate.DBSessionFactory;
import org.smartsnip.persistence.hibernate.Maintainance;
import org.apache.commons.cli.*;

/**
 * This class contains a console utility to reindex the full-text index files of
 * Lucene the the search engine used by hibernate.
 * 
 * @author Gerhard Aigner
 * 
 */
public class FullTextMassIndexer {

	/**
	 * Mass indexer for console use.
	 * <p>
	 * The available command line arguments are:
	 * <nl>
	 * <li><strong>-b=n</strong> or <strong>--batch=n</strong> Sets the batch
	 * size used to load the root entities (default <strong>25</strong>).</li>
	 * <li><strong>-t=n</strong> or <strong>--threads=n</strong> Set the number
	 * of threads to be used to load the root entities (default
	 * <strong>2</strong>).</li>
	 * <li><strong>-f=n</strong> or <strong>--id_fetch=n</strong> Specifies the
	 * fetch size to be used when loading primary keys if objects to be indexed.
	 * Some databases accept special values, for example MySQL might benefit
	 * from using {@link Integer#MIN_VALUE} otherwise it will attempt to preload
	 * everything in memory (default <strong>150</strong>).</li>
	 * <li><strong>-s=n</strong> or <strong>--sub_thread=n</strong> Sets the
	 * number of threads used to load the lazy collections related to the
	 * indexed entities (default <strong>1</strong>).</li>
	 * <li><strong>-h</strong> or <strong>--help</strong> Shows a help message.</li>
	 * </nl>
	 * 
	 * @param args
	 *            the command-line arguments.
	 */
	public static void main(String[] args) {

		// create Options
		CommandLineParser parser = new PosixParser();
		Options options = new Options();
		options.addOption("h", "help", false, "Prints this help message.");
		options.addOption(setNumberedOption("b", "batch", "SIZE",
				"Sets the batch size used to load "
						+ "the root entities (default 25)."));
		options.addOption(setNumberedOption("t", "threads", "NUM",
				"Sets the number of threads to be "
						+ "used to load the root entities (default 2)."));
		options.addOption(setNumberedOption("i", "id_fetch", "SIZE",
				"Specifies the fetch size "
						+ "to be used when loading primary "
						+ "keys if objects to be indexed (default 150)."));
		options.addOption(setNumberedOption("s", "sub_fetch", "NUM",
				"Sets the number of threads "
						+ "used to load the lazy collections related "
						+ "to the indexed entities (default 1)."));

		// parse the command line arguments
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				printHelpAndExit(options, 0);
			}
			int batchSizeToLoadObjects = getOptionValue("batch", line, 25);
			int threadsToLoadObjects = getOptionValue("threads", line, 2);
			int idFetchSize = getOptionValue("id_fetch", line, 150);
			int threadsForSubsequentFetching = getOptionValue("sub_fetch",
					line, 1);

			// rebuild the full text index
			try {
				Maintainance.fullTextMassIndexer(batchSizeToLoadObjects,
						threadsToLoadObjects, idFetchSize,
						threadsForSubsequentFetching);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			DBSessionFactory.closeFactory();
		} catch (ParseException exp) {
			System.out.println("Unexpected exception: " + exp.getMessage());
			printHelpAndExit(options, 0);
		}
	}

	/**
	 * Build an option with a number as value.
	 * 
	 * @param shortOpt
	 * @param longOpt
	 * @param argName
	 * @param description
	 * @return the option object
	 */
	private static Option setNumberedOption(String shortOpt, String longOpt,
			String argName, String description) {
		@SuppressWarnings("static-access")
		Option option = OptionBuilder.withLongOpt(longOpt)
				.withDescription(description).hasArg().withArgName(argName)
				.withType(Number.class).create(shortOpt);
		return option;
	}

	private static void printHelpAndExit(Options options, int exitCode) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("FullTextMassIndexer", options);
		System.exit(exitCode);
	}

	/**
	 * Get the value of an option if set and positive.
	 * 
	 * @param option
	 * @param line
	 * @param defaultValue
	 * @return the value set by the command line option or the default value.
	 * @throws ParseException
	 *             if the value coldn't be parsed or is < 1.
	 */
	private static int getOptionValue(String option, CommandLine line,
			int defaultValue) throws ParseException {
		Number value;
		if (line.hasOption(option)
				&& (value = (Number) line.getParsedOptionValue(option)) != null) {
			if (value.intValue() < 1) {
				throw new ParseException(
						"Positive number expected for option: --" + option);
			}
			return value.intValue();
		}
		return defaultValue;
	}
}
