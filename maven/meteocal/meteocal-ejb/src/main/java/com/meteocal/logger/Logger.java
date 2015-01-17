//package com.meteocal.logger;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import javax.ejb.Stateless;
//
///**
// * This class is a wrapper for java logger that we are using to log and the
// * debug field will be set to false in the final release.
// * 
// * @author Andrea Bignoli
// */
//@Stateless
//public class Logger {
//
//	/** The Debug flag. */
//	private static boolean Debug = true;
//
//	/** The show pings. */
//	private static boolean showPings = false;
//
//	/** The string filter. */
//	private static String filter = "";
//
//	/** The inclusive filters for the label. */
//	private static List<String> labelInclusiveFilters = new ArrayList<String>();
//
//	/** The exclusive filters for the label. */
//	private static List<String> labelExclusiveFilters = new ArrayList<String>();
//
//	/** The label filtering mode. */
//	private static LabelFiltering labelFiltering = LabelFiltering.EXCLUSIVE;
//
//	/** The exception logging flag. */
//	private static boolean exceptionLogging = true;
//
//	/** The label filtering status. */
//	private static boolean labelFilteringStatus = true;
//
//	/** The print stream to redirect the output to a file. */
//	private static PrintStream printStream;
//
//	/** The log file. */
//	private static File logFile;
//
//	/** The show wasted exceptions flag. */
//	private static boolean areWastedExceptionsShown = true;
//
//	/** The log default file name. */
//	private static String logFileName = "log";
//
//	/** The Constant file extension of the log file. */
//	private static final String LOG_FILE_EXTENSION = ".txt";
//
//	/** The Constant date formatting in the log file name. */
//	private static final String LOG_DATE_FORMATTING = "yyyy-MM-dd HH-mm-ss";
//
//	/**
//	 * This enum is used to control the label filtering mode of the logger:
//	 * 
//	 * INCLUSIVE: logs only the messages with a label included in
//	 * labelInclusiveFilters. EXCLUSIVE: logs only the messages with a label not
//	 * included in labelExclusiveFilters.
//	 * 
//	 * @author Andrea Bignoli
//	 * 
//	 */
//	public enum LabelFiltering {
//
//		/** The inclusive mode. */
//		INCLUSIVE,
//		/** The exclusive mode. */
//		EXCLUSIVE
//	}
//
//	/**
//	 * Instantiates a new logger.
//	 */
//	private Logger() {
//	}
//
//	/**
//	 * Sets the show pings.
//	 * 
//	 * @param showPings
//	 *            the new show pings status
//	 */
//	public static void setShowPings(boolean showPings) {
//		Logger.showPings = showPings;
//	}
//
//	/**
//	 * Standard getter.
//	 * 
//	 * @return the log file name
//	 */
//	public static String getLogFileName() {
//		return logFileName;
//	}
//
//	/**
//	 * Standard setter.
//	 * 
//	 * @param logFileName
//	 *            the new log base file name
//	 */
//	public static void setLogFileName(String logFileName) {
//		Logger.logFileName = logFileName;
//	}
//
//	/**
//	 * Write to file.
//	 * 
//	 * @throws IOException
//	 *             Signals that an I/O exception has occurred.
//	 */
//	public static void writeToFile() throws IOException {
//		String endString = LOG_FILE_EXTENSION;
//		String initString = "./Logs/" + dateString() + " " + logFileName;
//
//		File logsDir = new File("./Logs");
//		logsDir.mkdir();
//
//		logFile = new File(initString + endString);
//		int counter = 2;
//
//		// If a file with the given name exists, create another with a slightly
//		// different name
//		while (logFile.exists()) {
//			endString = " " + counter + LOG_FILE_EXTENSION;
//
//			logFile = new File(initString + endString);
//			counter++;
//		}
//
//		logFile.createNewFile();
//
//		printStream = new PrintStream(logFile);
//
//		if (exceptionLogging) {
//			System.setErr(printStream);
//		}
//	}
//
//	/**
//	 * Date string.
//	 * 
//	 * @return the date in a string format
//	 */
//	public static String dateString() {
//		return (new SimpleDateFormat(LOG_DATE_FORMATTING)).format(new Date());
//	}
//
//	/**
//	 * Show wasted exceptions.
//	 * 
//	 * @param value
//	 *            the new value of the flag
//	 */
//	public static void showWastedExceptions(boolean value) {
//		areWastedExceptionsShown = value;
//	}
//
//	/**
//	 * Logs an object to the console.
//	 * 
//	 * @param tolog
//	 *            the tolog
//	 * @deprecated Logs an object to the console. If that object is throwable
//	 *             its printstacktrace method will be called.
//	 */
//	@Deprecated
//	public static void log(Object tolog) {
//		if (Debug) {
//			if (tolog == null) {
//				printFunction("LOGGING NULL!?");
//				return;
//			}
//			java.util.logging.Logger.getGlobal().setLevel(java.util.logging.Level.ALL);
//			if (!tolog.toString().contains("Ping") || showPings) {
//				if (tolog instanceof Throwable) {
//					java.util.logging.Logger.getGlobal().log(java.util.logging.Level.SEVERE,
//							((Throwable) tolog).getMessage());
//					((Throwable) tolog).printStackTrace();
//				} else {
//					printFunction(tolog);
//				}
//
//			}
//		}
//	}
//
//	/**
//	 * Logs an object to the console. If that object is throwable its
//	 * printstacktrace method will be called.
//	 * 
//	 * @param label
//	 *            the label
//	 * @param tolog
//	 *            the tolog
//	 */
//	public static void log(String label, Object tolog) {
//		if (Debug) {
//			if (tolog == null) {
//				printFunction(label + "LOGGING NULL");
//				return;
//			}
//			if (!isValid(label)) {
//				return;
//			}
//			java.util.logging.Logger.getGlobal().setLevel(java.util.logging.Level.SEVERE);
//			if (!tolog.toString().contains("Ping") || showPings) {
//				if (tolog instanceof Throwable) {
//					printFunction(label);
//					java.util.logging.Logger.getGlobal().log(java.util.logging.Level.SEVERE,
//							((Throwable) tolog).getMessage());
//					((Throwable) tolog).printStackTrace();
//				} else {
//					printFunction(label + tolog);
//				}
//
//			}
//		}
//	}
//
//	/**
//	 * Wastes an exception.
//	 * 
//	 * @param toWaste
//	 *            the exception to waste
//	 * @deprecated call wasteException(label,e) instead
//	 */
//	@Deprecated
//	public static void wasteException(Throwable toWaste) {
//		if (areWastedExceptionsShown) {
//			java.util.logging.Logger.getGlobal().log(java.util.logging.Level.WARNING, "WASTED: ", toWaste);
//		}
//		java.util.logging.Logger.getGlobal().log(java.util.logging.Level.FINE, "", toWaste);
//
//	}
//
//	/**
//	 * This should be used to destroy data about useless exception.
//	 * 
//	 * @param label
//	 *            the label
//	 * @param toWaste
//	 *            the exception to waste
//	 */
//	public static void wasteException(String label, Throwable toWaste) {
//
//		if (areWastedExceptionsShown) {
//			java.util.logging.Logger.getGlobal().log(java.util.logging.Level.WARNING, "WASTED: " + label,
//					toWaste);
//		}
//
//		java.util.logging.Logger.getGlobal().log(java.util.logging.Level.FINE, label, toWaste);
//
//	}
//
//	/**
//	 * Gets the Debug flag value.
//	 * 
//	 * @return true, if is debug
//	 */
//	public static synchronized boolean isDebug() {
//		return Debug;
//	}
//
//	/**
//	 * Enables logging.
//	 */
//	public static synchronized void enable() {
//		Debug = true;
//		log("[DEBUG_OUTPUT]", "Is enabled");
//	}
//
//	/**
//	 * Disables Logging.
//	 */
//	public static synchronized void disable() {
//		Debug = false;
//	}
//
//	/**
//	 * Sets a filter for the logger. The logger will always do a check if the
//	 * string to be printed contains the filter. Leave null to print everything.
//	 * 
//	 * @param filter
//	 *            the new filter
//	 */
//	public static void setFilter(String filter) {
//		Logger.filter = filter;
//	}
//
//	/**
//	 * Removes useless info and prints the given object calling the tostring()
//	 * method.
//	 * 
//	 * @param tolog
//	 *            the tolog
//	 */
//	private static void printFunction(Object tolog) {
//
//		String string = tolog.toString().replace(
//				"it.polimi.deib.provaFinale2014.ilaria_cislaghi_roberto_clapis_andrea_bignoli.", "");
//		string = string.replace(
//				"CLASS IT.POLIMI.DEIB.PROVAFINALE2014.ILARIA_CISLAGHI_ROBERTO_CLAPIS_ANDREA_BIGNOLI.", "");
//		tolog = string;
//		if (tolog.toString().contains(filter)) {
//			System.out.println("$ " + tolog.toString());
//			// java.util.logging.Logger.getGlobal().info(
//			// tolog.toString());
//		}
//		if (printStream != null) {
//			printStream.println("$ " + tolog.toString());
//		}
//	}
//
//	/**
//	 * Adds a filter to exclusive filters.
//	 * 
//	 * @param labelExclusiveFilter
//	 *            Filter to add
//	 */
//	public static void addLabelExclusiveFilter(String labelExclusiveFilter) {
//		labelExclusiveFilters.add(labelExclusiveFilter);
//	}
//
//	/**
//	 * Removes a filter from exclusive filters.
//	 * 
//	 * @param labelExclusiveFilter
//	 *            Filter to remove
//	 */
//	public static void removeLabelExclusiveFilter(String labelExclusiveFilter) {
//		labelExclusiveFilters.remove(labelExclusiveFilter);
//	}
//
//	/**
//	 * Adds a filter to inclusive filters.
//	 * 
//	 * @param labelInclusiveFilter
//	 *            Filter to add
//	 */
//	public static void addLabelInclusiveFilter(String labelInclusiveFilter) {
//		labelInclusiveFilters.add(labelInclusiveFilter);
//	}
//
//	/**
//	 * Removes a filter from inclusive filters.
//	 * 
//	 * @param labelInclusiveFilter
//	 *            Filter to remove
//	 */
//	public static void removeLabelInclusiveFilter(String labelInclusiveFilter) {
//		labelInclusiveFilters.remove(labelInclusiveFilter);
//	}
//
//	/**
//	 * Tells if the given label is allowed for printing purposes.
//	 * 
//	 * @param label
//	 *            The label to evaluate
//	 * @return True if the label is valid, false if not
//	 */
//	private static boolean isValid(String label) {
//		if (!labelFilteringStatus) {
//			return true;
//		}
//
//		if (labelFiltering == LabelFiltering.EXCLUSIVE) {
//			return !labelExclusiveFilters.contains(label);
//		} else if (labelFiltering == LabelFiltering.INCLUSIVE) {
//			return labelInclusiveFilters.contains(label);
//		}
//
//		return true;
//	}
//
//	/**
//	 * Sets the label filtering mode to the one given.
//	 * 
//	 * @param mode
//	 *            The new label filtering mode
//	 */
//	public static void setLabelFiltering(LabelFiltering mode) {
//		labelFiltering = mode;
//	}
//
//	/**
//	 * Standard setter.
//	 * 
//	 * @param status
//	 *            True to enable label filtering, false to disable it
//	 */
//	public static void setLabelFilteringStatus(boolean status) {
//		labelFilteringStatus = status;
//	}
//
//	/**
//	 * Sets file logging for exceptions. Must be called before writeToFile
//	 * 
//	 * @param active
//	 *            the new exception logging
//	 * @throws IOException
//	 *             Signals that an I/O exception has occurred.
//	 */
//	public static void setExceptionLogging(boolean active) throws IOException {
//		if (!exceptionLogging && active) {
//			System.setErr(printStream);
//		} else if (exceptionLogging && !active) {
//			System.setErr(System.out);
//		}
//
//		exceptionLogging = active;
//	}
//}
