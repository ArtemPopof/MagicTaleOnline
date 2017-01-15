package com.p3k.magictale.engine;

/**
 * Created by jorgen on 24.12.16.
 */

import client.ClientGame;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger can write log to out stream
 * Log differs when debug or release
 */
public class Logger {
    private static PrintStream infoStream;
    private static PrintStream debugStream;
    private static PrintStream errorStream;

    static {
        infoStream = System.out;
        debugStream = System.out;
        errorStream = System.err;
    }

    public static void setInfoStream(PrintStream infoStream) {
        Logger.infoStream = infoStream;
    }

    public static void setDebugStream(PrintStream debugStream) {
        Logger.debugStream = debugStream;
    }

    public static void setErrorStream(PrintStream errorStream) {
        Logger.errorStream = errorStream;
    }

    public final static int DEBUG = 0;
    public final static int INFO = 1;
    public final static int ERROR = 2;

    public static void log(String msg, int type) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        switch (type) {
            case INFO:
                msg = "(INFO) " + time + ": " + msg;
                infoStream.println(msg);
                break;
            case DEBUG:
                if ( !ClientGame.isDebug() ) {
                    return;
                }

                msg = "(DEBUG) " + time + ": " + msg;
                debugStream.println(msg);
                break;
            case ERROR:
                msg = "(ERROR) " + time + ": " + msg;
                errorStream.println(msg);
                break;
        }
    }

}
