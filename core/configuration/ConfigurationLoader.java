package com.websystem.core.configuration;

import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.util.Properties;

public class ConfigurationLoader {

    @SneakyThrows
    public static Properties loadConfiguration ( String name, String destination ) {
        String propName = name + ".properties";
        File file = new File( destination + "/" + propName );
        if ( !file.exists() ) {
            file.getParentFile().mkdirs();
            System.out.println( "Creating Configuration File from resources (" + propName + ")" );
            Files.copy( ConfigurationLoader.class.getClassLoader().getResourceAsStream( propName ), file.toPath() );
        }
        return loadConfiguration( file );
    }

    @SneakyThrows
    public static Properties loadConfiguration ( File file ) {
        if ( !file.exists() ) return null;
        System.out.println( "Reading Configuration File " + file );
        Properties properties = new Properties();
        properties.load( Files.newBufferedReader( file.toPath() ) );
        return properties.isEmpty() ? null : properties;
    }
}
