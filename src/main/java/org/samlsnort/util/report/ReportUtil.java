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
package org.samlsnort.util.report;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.samlsnort.vo.TestSuite;

public class ReportUtil {
	private static final Logger LOGGER = Logger.getLogger(ReportUtil.class
			.getName());

	 private static final ResourceBundle RESORUCE = ResourceBundle
	 .getBundle(ReportUtil.class.getName());

	public static JasperPrint createJasperPrint(TestSuite testSuite)
			throws JRException {
		LOGGER.entering(ReportUtil.class.getName(), "createJasperPrint",
				testSuite);

		Map<Object, Object> properties = new HashMap<Object, Object>();
		properties.put("REPORT_TITLE", RESORUCE.getString("reportName"));
		properties.put("SUBREPORT_INPUT_STREAM", ReportUtil.class
				.getResourceAsStream("subreport.jasper"));

		JasperPrint print = JasperFillManager
		.fillReport(ReportUtil.class.getResourceAsStream("report.jasper"),
				properties, new JRBeanArrayDataSource(
						new TestSuite[] { testSuite }));

		LOGGER.exiting(ReportUtil.class.getName(), "createJasperPrint", print);
		return print;
	}
}
