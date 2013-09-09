/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kjcjohnson.kp.apps.LJWE;

import com.kjcjohnson.kp.tools.cc.CC;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

/**
 * This class is spawned by the Main server for each lisp application. It deals 
 * with the communication with the lisp host.
 * @author Keith
 */
public class NetworkDaemon extends Thread {
    
    /**
     * Sent to client to initiate initialization handshake
     */
    public byte[] INIT_INIT = { CC.BSTX, CC.BENQ, CC.BETX};
    /**
     * Returned from client to return handshake
     */
    public byte[] HANDSHAKE_RET = { CC.BSTX, CC.BACK, CC.BETX };
    /**
     * Sent to client to indicate server ready for commands
     */
    public byte[] CONNECTION_SUCCESS = { CC.BSTX, CC.BBEL, CC.BETX };
                        
    /**
     * This is the network interface to the lisp client.
     */        
    private transient Socket netsock;
    
    /**
     * Creates a new NetworkDaemon with a given socket.
     * @param netsock a network socket connected to the lisp application.
     */
    
    public NetworkDaemon( Socket netsock ) {
        
        this.netsock = netsock;
        
    }
    
    /**
     * This method initializes the network connection and 
     * starts the windowing environment in a fresh box.
     */
    @Override
    public void run() {
        
        InputStream input;
        OutputStream output;
        
        System.out.println( "Connected!" );
        
        try {
            
            input = netsock.getInputStream();
            output = netsock.getOutputStream();
            
        } catch (IOException ioe) {
            System.err.println("Error creating socket streams.\n\nTerminating daemon.");
            return;
        }
        
        try {
            
            output.write(INIT_INIT);
            output.flush();
            byte[] buf = new byte[3];
            input.read(buf);
            if (    (HANDSHAKE_RET[0] != buf[0]) 
                 || (HANDSHAKE_RET[1] != buf[1]) 
                 || (HANDSHAKE_RET[2] != buf[2])) {
                throw new IncorrectResponseException("Handshake Returned did not match");
            } 
            output.write(CONNECTION_SUCCESS);
            output.flush();
            
        } catch (IOException ioe) {
            System.err.println( "IO error with handshake.\n\nTerminating daemon." );
            return;
        } catch (IncorrectResponseException e) {
            System.err.println( "Incorrect handshake response.\n\nProceding recklessly.");
        }
        
        boolean run = true;
        WindowingEnvironment we = new WindowingEnvironment();
        
        while( run ) {
        	String response = "repo";
        	try {
        		StringBuilder sb = new StringBuilder();
        		char inp;
        		while ((inp = Character.toChars( input.read() )[0]) != CC.ETX) {
        			sb.append(inp);        			
        		}
        		
        		System.out.println( sb.toString() ); 
        		StringTokenizer token = new StringTokenizer( sb.toString(), " " );
        		
        		try {
        		
        			switch (token.nextToken()) {
        		
        				case "int_create":
        					response = we.createInteger( Integer.parseInt(token.nextToken()) );
        					break;
        				case "int_read":
        					response = we.readInteger(token.nextToken()).toString();
        					break;
        				case "int_update":
        					response = we.updateInteger(token.nextToken(), Integer.parseInt(token.nextToken())).toString();
        					break;
        				case "int_destroy":
        					response = we.destroyInteger(token.nextToken()).toString();
        					break;
        					
        				case "class_create":
        					response = we.createClass(token.nextToken());
        					break;
        				case "class_read":
        					response = we.readClass(token.nextToken()).toString();
        					break;
        				case "class_update":
        					response = we.updateClass(token.nextToken(), token.nextToken()).toString();
        					break;
        				case "class_destroy":
        					response = we.destroyClass(token.nextToken()).toString();
        					break;
        			}
        			
        		} catch (Exception e) {
        			
        			System.out.println( "Null exception" );
        			response = "NULL";
        			
        		}
        		
        	} catch (Exception ioe) {
        		ioe.printStackTrace();
        	} finally {
        		try {
        			output.write(response.getBytes());
        			output.flush();
        			System.out.println( response );
        		} catch (IOException ioe) {
        			
        		}
        	}
        	
        	
        }
        
    }
    
}

/**
 * This exception is thrown when a response received does not match the 
 * expected data.
 */
class IncorrectResponseException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3514300107271330929L;

	IncorrectResponseException( String desp ) {
        super( desp );
    }

}