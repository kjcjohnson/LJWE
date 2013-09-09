/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kjcjohnson.kp.apps.LJWE;

import java.io.*;
import java.net.*;

/**
 *
 * @author Keith
 */

public class Main {
	
	public boolean quit = false;
    
    /**
     * 
     * @param args 
     */
    public static void main( String args[] ) {
    
    	new Main();
    	
    } 
    
    public Main() {
    	
        String hostname = "localhost";
        int port = 7282;
        ServerSocket server;
        try {
            server = new ServerSocket();
            server.bind( new InetSocketAddress(hostname, port));
        } catch (IOException ioe) {
            System.out.println("Error creating server\n\nProgram terminated.");
            return;
        }
            
        while (!quit) {
            
            Socket nextSocket;
            
            try {
                nextSocket = server.accept();
            } catch (IOException ioe) {
                System.err.println("Connection failed.");
                continue;
            }
            new NetworkDaemon( nextSocket ).start();
            
            
        }
        try {
        	server.close();
        } catch (IOException ioe) {
        	System.err.println("Failed closing server");
        }
    }
    
}
