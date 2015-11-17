package bo.com.qbit.webapp.validator;

// Validator1
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Empresa;

public abstract class Validator {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
			"[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
			"(\\.[A-Za-z]{2,})$";
	private static final String LETTER_PATTERN = "[^A-Za-z ]";
	@Inject
	private EntityManager em;
	private Pattern pattern;
	private Matcher matcher;

	public Validator(){
	}

	public boolean isEmppty(Object object){
		return object.toString().length()<1?true:false;
	}

	public boolean exist(Object object,String item,String value){
		try{
			String query = "select em from "+object+" em where em."+item+"="+value;
			System.out.println("query: "+query);
			Object obj =  em.createQuery(query).getSingleResult();
			return obj!=null?true:false;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean existByEmpresa(Object object,String item,String value,Empresa empresa){
		try{
			String query = "select em from "+object+" em where em."+item+"="+value+" and em.empresa.id="+empresa.getId();
			System.out.println("query: "+query);
			Object obj =  em.createQuery(query).getSingleResult();
			return obj!=null?true:false;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean isNumeric(Object value){
		try {
			Integer.parseInt((String) value);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}

	public boolean isLetter(Object value){
		pattern = Pattern.compile(LETTER_PATTERN);
		matcher = pattern.matcher(value.toString());
		if(!matcher.find()){
			return true;
		}
		return false;
	}

	public boolean isValidateEmail(Object value) throws ValidatorException {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(value.toString());
		if(matcher.matches()){
			return false;
		}
		return true;
	}
}