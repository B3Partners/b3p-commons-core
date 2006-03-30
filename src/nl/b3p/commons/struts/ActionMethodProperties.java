/*
 * $Id: ActionMethodProperties.java 2770 2006-03-01 13:24:31Z Matthijs $
 */

package nl.b3p.commons.struts;

public abstract class ActionMethodProperties {
	private String methodName;
	
	public ActionMethodProperties(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
