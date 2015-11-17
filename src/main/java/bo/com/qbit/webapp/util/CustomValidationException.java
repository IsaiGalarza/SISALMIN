package bo.com.qbit.webapp.util;

public class CustomValidationException extends Exception {

	private static final long serialVersionUID = 7761719664499714959L;

	public CustomValidationException(String msg) {
		super(msg);
	}
	
	public CustomValidationException(String msg, Throwable ex) {
		super(msg,ex);
	}
	
}