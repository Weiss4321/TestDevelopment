/**
 * 
 */
package hr.vestigo.modules.collateral.batch.bo30;

/**
 * @author hraamh
 *
 */
public class IgnoreException extends Exception {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo30/IgnoreException.java,v 1.1 2008/09/05 11:42:14 hraamh Exp $";

	/**
	 * 
	 */
	public IgnoreException() {
	}

	/**
	 * @param message
	 */
	public IgnoreException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public IgnoreException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IgnoreException(String message, Throwable cause) {
		super(message, cause);
	}

}
