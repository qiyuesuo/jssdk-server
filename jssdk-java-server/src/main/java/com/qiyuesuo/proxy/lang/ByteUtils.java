package com.qiyuesuo.proxy.lang;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteUtils {

	public static final String PREFERRED_ENCODING = "UTF-8";
	
	private static final Logger logger = LoggerFactory.getLogger(ByteUtils.class);
	
	public static final byte[] EMPTY = new byte[0];

	public static byte[] toBytes(char[] chars) {
		return toBytes(new String(chars), PREFERRED_ENCODING);
	}

	public static byte[] toBytes(char[] chars, String encoding) {
		return toBytes(new String(chars), encoding);
	}

	public static byte[] toBytes(String source) {
		return toBytes(source, PREFERRED_ENCODING);
	}

	public static byte[] toBytes(String source, String encoding) {
		try {
			return source.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			String msg = "Unable to convert source [" + source + "] to byte array using " + "encoding '" + encoding + "'";
			logger.warn(msg,e);
			return source.getBytes();
		}
	}
	
	public static byte[] toBytes(Object o) {
        if (o == null) {
            String msg = "Argument for byte conversion cannot be null.";
            throw new IllegalArgumentException(msg);
        }
        if (o instanceof byte[]) {
            return (byte[]) o;
        } else if (o instanceof char[]) {
            return toBytes((char[]) o);
        } else if (o instanceof String) {
            return toBytes((String) o);
        } else if (o instanceof File) {
            return toBytes((File) o);
        } else if (o instanceof InputStream) {
            return toBytes((InputStream) o);
        } else {
            return objectToBytes(o);
        }
    }
	
	protected static byte[] toBytes(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File argument cannot be null.");
        }
        try {
            return toBytes(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            String msg = "Unable to acquire InputStream for file [" + file + "]";
            logger.error(msg,e);
            return EMPTY;
        }
    }
	
	protected static byte[] toBytes(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("InputStream argument cannot be null.");
        }
        final int BUFFER_SIZE = 512;
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        try {
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return out.toByteArray();
        } catch (IOException ioe) {
        		logger.error(ioe.getMessage(), ioe);
            return EMPTY;
        } finally {
            try {
                in.close();
            } catch (IOException ignored) {
            }
            try {
                out.close();
            } catch (IOException ignored) {
            }
        }
    }
	
	protected static byte[] objectToBytes(Object o) {
        String msg = "The " + o.getClass().getName() + " implementation only supports conversion to " +
                "byte[] if the source is of type byte[], char[], String, " +
        		    " File or InputStream.  The instance provided as a method " +
                "argument is of type [" + o.getClass().getName() + "].  If you would like to convert " +
                "this argument type to a byte[], you can 1) convert the argument to one of the supported types " +
                "yourself and then use that as the method argument or 2) subclass " + o.getClass().getName() +
                "and override the objectToBytes(Object o) method.";
        logger.error(msg);
        
        return EMPTY;
    }
	
    public static String toString(byte[] bytes) {
        return toString(bytes, PREFERRED_ENCODING);
    }

    private static String toString(byte[] bytes, String encoding){
        try {
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            String msg = "Unable to convert byte array to String with encoding '" + encoding + "'.";
            logger.error(msg,e);
            return null;
        }
    }

}
