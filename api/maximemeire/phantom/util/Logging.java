package maximemeire.phantom.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Logging {
    /**
     * Creates a new <code>Logger</code> object and names it based on the class
     * that this method was called.
     *
     * @return The <code>Logger</code> object
     */
    public static Logger log() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        Logger logger = Logger.getLogger(caller.getClassName());
        return logger;
    }
    
    /**
     * Sets up the logger.
     */
    public static void setup(String appName) {
        System.out.println("//***************************************************************************\\\\");
        System.out.println("   PhantomRS [" + appName + "]");
        System.out.println("   Copyright (C) Maxime Meire 2012");
        System.out.println("   Running " + System.getProperty("os.name") + " on a(n) " + System.getProperty("os.arch") + " architecture");
        System.out.println("   Java version is " + System.getProperty("java.version"));
        System.out.println("\\\\***************************************************************************//");
        System.out.println();

        PropertyConfigurator.configure("./data/log4j.properties");
    }
}
