/*******************************************************************************
 * Copyright (C) 2011 by Michael Gerber
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package org.samlsnort.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.binary.Base64;

public class EncodingTool {
	
	private static final Charset CHARSET;

	static {
		CHARSET = Charset.forName("UTF-8");
	}
	
	private EncodingTool() {
	}
	
	public static String fromBase64(String b64) {
		return new String(Base64.decodeBase64(b64.getBytes(CHARSET)), CHARSET);
	}
	
	public static String toBase64(String input) {
		return toBase64(input.getBytes(CHARSET));
	}
	
	public static String toBase64(byte[] data) {
		return new String(Base64.encodeBase64(data), CHARSET);
	}
	
	public static String inflate(byte[] deflated) throws DataFormatException {
		Inflater inflater = new Inflater(true);
		inflater.setInput(deflated);
		byte[] inflatedBytes = new byte[4096];
		int len = inflater.inflate(inflatedBytes);
		inflater.setInput(new byte[0]);
		len += inflater.inflate(inflatedBytes, len, 1);
		inflater.end();
		return new String(inflatedBytes, 0, len, CHARSET);
	}
	
	
	public static String inflateFromBase64(String deflated) throws DataFormatException {
		return EncodingTool.inflate(Base64.decodeBase64(deflated.getBytes(CHARSET)));
	}

	public static String deflateToBase64(String inflated) {
		
		Deflater deflater = new Deflater(Deflater.DEFLATED, true);
		deflater.setInput(inflated.getBytes(CHARSET));
		deflater.finish();
		byte[] deflatedBytes = new byte[2048];
		int len = deflater.deflate(deflatedBytes);
		return new String(Base64.encodeBase64(Arrays.copyOf(deflatedBytes, len)), CHARSET);
	}
	
	public static String urlEncode(String input) {
		try {
			return URLEncoder.encode(input, CHARSET.name());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	public static String urlDecode(String input) throws UnsupportedEncodingException {
		return URLDecoder.decode(input, CHARSET.name());
	}
	

}
