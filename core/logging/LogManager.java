package com.websystem.core.logging;

import lombok.Getter;
import org.apache.log4j.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

public class LogManager {

    private static final Logger logger = Logger.getLogger( "CloudSystem" );
    private static boolean init = false;
    private static PrintStream oldOut, oldErr;
    @Getter
    private static File logFile;

    public static void init () {
        if ( init ) return;
        Layout layout = new PatternLayout( "%d{dd.MM.yyyy HH:mm:ss} - %m%n" );
        oldOut = System.out;
        oldErr = System.err;
        System.setOut( new PrintStream( new LogOutputStream( logger, Level.INFO, oldOut ), true ) );
        System.setErr( new PrintStream( new LogOutputStream( logger, Level.ERROR, oldErr ), true ) );

        try {
            String logFileName = generateLogFileName( new File( "logs/" ) );
            logFile = new File( logFileName );
            FileAppender fileAppender = new FileAppender( layout, logFileName, false );
            logger.addAppender( fileAppender );
            logger.setAdditivity( false );
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        init = true;
    }

    private static String generateLogFileName ( File folder ) {
        String date = new SimpleDateFormat( "ddMMyyyy" ).format( System.currentTimeMillis() );
        String fileName = null;

        for ( int id = 0; id >= 0; id++ ) {
            fileName = date + id;
            if ( !new File( folder, fileName + ".log" ).exists() ) break;
        }

        return folder.getPath() + "/" + fileName + ".log";
    }
}
