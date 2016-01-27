package thehambone.gtatools.gta3savefileeditor.gui;

import thehambone.gtatools.gta3savefileeditor.util.GUIUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import thehambone.gtatools.gta3savefileeditor.io.IO;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Catches unexpected exceptions during runtime. Unexpected exceptions are
 * typically life-threatening to the running instance of the program, so the
 * program will terminate after displaying an error message and dumping the
 * contents of stdout and stderr to a file.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 12, 2015
 */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler
{
    @Override
    public void uncaughtException(Thread t, Throwable e)
    {
        e.printStackTrace(IO.getStderr());
        File file = new File(String.format("crash-report_%s.log", new SimpleDateFormat("yyyymmddhhmmss").format(new Date())));
        boolean fileWritten = false;
        try {
            IO.saveOutput(file);
            fileWritten = true;
        } catch (IOException ex) {
            // Shouldn't happen.
            // Since we are already in the uncaught exception handler, throwing
            // a RuntimeException would be redundant. Instead, we won't say that
            // a crash report was generated and just output the IOException to
            // stderr.
            Logger.error("Failed to write file.");
            Logger.stackTrace(ex);
        }
        String errorMessage;
        if (fileWritten) {
            errorMessage = String.format("A critical error has occured and the program needs to close. "
                    + "A log file has been created at %s.\n\n"
                    + "The error occured in the following thread:\n%s", file.getAbsolutePath(), t.getName());
        } else {
            errorMessage = String.format("A critical error has occured and the program needs to close.\n\n"
                    + "The error occured in the following thread:\n%s", t.getName());
        }
        Logger.fatal(errorMessage);
        Logger.stackTrace(e);
        GUIUtils.showErrorMessage(null, errorMessage, "Critical Error", 300, e);
        System.exit(1);     // Exit codes don't have any meaning yet, so a
                            // simple nonzero exit code will do.
    }
}