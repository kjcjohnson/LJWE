package com.kjcjohnson.kp.apps.LJWE;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;


public class WindowingEnvironment {

	public HashMap<String, Class  > classes;
	public HashMap<String, Integer> integers;
 	public HashMap<String, Object > objects;	
 	
 	private SecureRandom classrand;
 	private SecureRandom intrand;
 	private SecureRandom objrand;
 	
 	private String gen_id( SecureRandom which ) {
 		return new BigInteger(130, which).toString(32);
 	}
 	
	public WindowingEnvironment() {
		
		classrand = new SecureRandom();
		intrand   = new SecureRandom();
		objrand   = new SecureRandom();
		
		classes  = new HashMap<String, Class  >();
		integers = new HashMap<String, Integer>();
		objects  = new HashMap<String, Object >();
		
	}
	
	public String createInteger( int i ) {
		String key = gen_id( intrand );
		integers.put(key, new Integer(i));
		return key;
	}
	public Integer readInteger( String key ) {
		
		return integers.get(key);
			
	}
	public Integer updateInteger( String key, int i ) {
		
		Integer retval = integers.get( key );
		integers.remove( key );
		integers.put( key, i );
		return retval;
		
	}	
	public Integer destroyInteger( String key ) {
		
		Integer retval = integers.get( key );
		integers.remove( key );
		return retval;
		
	}
	
	public String createClass( String classname ) {
		
		Class cla;
		String key = gen_id( classrand );
		try {
			cla  = Class.forName(classname);
		} catch (ClassNotFoundException cnfe) {
			return null;
		}
		classes.put(key, cla);
		return key;
	}
	public Class readClass( String key ) {
		return classes.get(key);
	}
	public Class updateClass( String key, String classname ) {
		Class retval = classes.get(key);
		try {
			Class newclass = Class.forName(classname);
			classes.remove(key);
			classes.put(key, newclass);
		} catch (ClassNotFoundException cnfe) {
			return null;
		}
		return retval;
	}
	public Class destroyClass( String key ) {
		Class retval = classes.get(key);
		classes.remove(key);
		return retval;
	}
}
