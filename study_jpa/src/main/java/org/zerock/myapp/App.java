package org.zerock.myapp;


import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
public class App 
{
    public static void main( String[] args ) {
        log.trace("main({}) invoked.", Arrays.toString(args));

    } // main

} // end class
