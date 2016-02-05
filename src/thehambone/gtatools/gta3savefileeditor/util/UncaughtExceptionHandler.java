package thehambone.gtatools.gta3savefileeditor.util;

import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.Main;

/**
 * Catches unexpected exceptions during runtime. Unexpected exceptions are
 * typically life-threatening to the program, so the JVM will terminate with a
 * nonzero exit code after displaying an error message and dumping the program
 * log to a file.
 * <p>
 * Created on Apr 12, 2015.
 * 
 * @author thehambone
 */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler
{
    @Override
    public void uncaughtException(Thread t, Throwable e)
    {
        String errorMessage1 = "A critical error has occured and the program "
                + "needs to close. The error occured in the following thread:"
                + "\n" + t.getName();
        Logger.fatal(errorMessage1.replaceAll("\\\n", " "), e);
        Logger.stackTrace(Logger.Level.FATAL, e);
        
        String crashDumpFileName = null;
        try {
            crashDumpFileName = Logger.generateCrashDump();
        } catch (IOException ex) {
            /* Since we are already in the uncaught exception handler, throwing
               a RuntimeException would be redundant. Instead, we won't say that
               a crash report was generated and just output the IOException to
               stderr. */
            Logger.error("Failed to write crash dump file.", ex);
            Logger.stackTrace(ex);
        }
        
        String errorMessage2 = "";
        if (crashDumpFileName != null) {
            errorMessage2 = "\n\nA crash dump has been created at "
                    + crashDumpFileName + ".\nIf the problem persists, please "
                    + "contact the developer at " + Main.PROGRAM_AUTHOR_EMAIL
                    + ".";
        }
        
        GUIUtilities.showErrorMessageBox(null,
                errorMessage1 + errorMessage2,
                "Critical Error", e, 300, true);
        
        Logger.warn("Exiting JVM with exit code 1...");
        System.exit(1);
    }
}
