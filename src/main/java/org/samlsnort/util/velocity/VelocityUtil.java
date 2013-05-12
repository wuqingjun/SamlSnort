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
package org.samlsnort.util.velocity;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class VelocityUtil {

	private static final Logger LOGGER = Logger.getLogger(VelocityUtil.class
			.getName());
	private static Template FORM_TEMPLATE = null;

	static {

		VelocityEngine engine = new VelocityEngine();
		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p
					.setProperty("class.resource.loader.class",
							"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			engine.init(p);
			FORM_TEMPLATE = engine.getTemplate("org/samlsnort/util/velocity/form.vm");

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error while initializing Velocity Tool.", e);
		}
	}

	public static void writeForm(Writer writer, String destination,
			String relayState, String encodedResponse)
			throws ResourceNotFoundException, ParseErrorException,
			MethodInvocationException, IOException {
		LOGGER
				.entering(VelocityUtil.class.getName(), "writeForm",
						new Object[] { writer, destination, relayState,
								encodedResponse });

		VelocityContext context = new VelocityContext();
		context.put("destination", destination);
		context.put("encodedResponse", encodedResponse);
		context.put("relayState", relayState);
		FORM_TEMPLATE.merge(context, writer);
		writer.flush();

		LOGGER.exiting(VelocityUtil.class.getName(), "writeForm");
	}

}
