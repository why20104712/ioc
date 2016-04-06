package cn.why.ioc;

import cn.why.A;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
//		Map<String, Bean> xmlBean = ConfigManager.parseXmlBean("applicationContext.xml");
//		System.out.println(xmlBean.toString());
		
		ClassPathApplicationContext applicationContext = new ClassPathApplicationContext();
		A bean = (A) applicationContext.getBean("aObject");
		System.out.println(bean.getId());
	}
}
