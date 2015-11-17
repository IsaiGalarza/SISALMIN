package bo.com.qbit.webapp.util;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="messagesBean")
@RequestScoped
public class MessagesBean extends AbstractManagedBean implements Serializable{

	private static final long serialVersionUID = 8386076001608276180L;

	public boolean isError() {
		return !getCurrentContext().getMessageList().isEmpty();
	}
}
