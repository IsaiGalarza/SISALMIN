package bo.com.qbit.webapp.util;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;


public class AbstractManagedBean {

	protected FacesContext getCurrentContext() {
		return FacesContext.getCurrentInstance();
	}

	public void addInfo(String summary,String detail) {
		addMessage(summary,detail, FacesMessage.SEVERITY_INFO);
	}

	public void addError(String summary,String detail) {
		addMessage(summary,detail, FacesMessage.SEVERITY_ERROR);
	}

	private void addMessage(String summary,String detail,Severity severity) {
		
		FacesMessage message=new FacesMessage(severity,summary,detail);
		FacesContext ctx=getCurrentContext();
		ctx.addMessage(null, message);
	}

	public String getMessage(String key) {
		return (String)getExpression("label['"+key+"']");
	}

	private Object getExpression(String expression) {
		FacesContext ctx = getCurrentContext();
		ExpressionFactory factory = ctx.getApplication().getExpressionFactory();
		ValueExpression ex = factory.createValueExpression(ctx.getELContext(), "#"+"{" + expression +"}", Object.class);
		return ex.getValue(ctx.getELContext());
	}
}