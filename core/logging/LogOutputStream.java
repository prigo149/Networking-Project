package com.websystem.core.logging;

import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

public class LogOutputStream extends OutputStream {

    protected static final String LINE_SEPERATOR = System.getProperty( "line.separator" );
    protected boolean hasBeenClosed = false;

    protected byte[] buf;

    protected int count;

    private int bufLength;

    public static final int DEFAULT_BUFFER_LENGTH = 2048;

    @Getter
    @Setter
    private Logger logger;
    @Getter
    @Setter
    private Level level;
    private PrintStream oldOut;

    public LogOutputStream ( Logger logger, Level level, PrintStream oldOut ) {
        this.logger = logger;
        this.level = level;
        this.oldOut = oldOut;
        bufLength = DEFAULT_BUFFER_LENGTH;
        buf = new byte[ DEFAULT_BUFFER_LENGTH ];
        count = 0;
    }

    public void close () {
        flush();
        hasBeenClosed = true;
    }

    public void write ( int b ) throws IOException {
        if ( hasBeenClosed ) throw new IOException( "The stream has been closed." );

        if ( b == 0 ) return;

        if ( count == bufLength ) {
            final int newBufLength = bufLength + DEFAULT_BUFFER_LENGTH;
            final byte[] newBuf = new byte[ newBufLength ];

            System.arraycopy( buf, 0, newBuf, 0, bufLength );

            buf = newBuf;
            bufLength = newBufLength;
        }

        buf[ count ] = ( byte ) b;
        count++;
    }

    public void flush () {
        if ( count == 0 ) return;

        if ( count == LINE_SEPERATOR.length() ) {
            if ( ( ( char ) buf[ 0 ] ) == LINE_SEPERATOR.charAt( 0 ) && ( ( count == 1 ) ||
                    ( ( count == 2 ) && ( ( char ) buf[ 1 ] ) == LINE_SEPERATOR.charAt( 1 ) ) ) ) {
                reset();
                return;
            }
        }

        byte[] theBytes = new byte[ count ];

        System.arraycopy( buf, 0, theBytes, 0, count );

        logger.log( level, new String( theBytes ) );
        oldOut.println( new SimpleDateFormat( "HH:mm:ss" ).format( System.currentTimeMillis() ) + " - " + new String( theBytes ) );

        reset();
    }

    private void reset () {
        count = 0;
    }

}
