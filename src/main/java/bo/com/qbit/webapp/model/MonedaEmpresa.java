package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Class MonedaEmpresa
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "moneda_empresa", schema = "public")
public class MonedaEmpresa implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="simbolo",nullable=true )
	private String simbolo;
	
	@Column(name="tipo",nullable=true )
	private String tipo;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_moneda", nullable=false)
	private Moneda moneda;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;

	@Size(max = 2) //AC , IN
	private String estado ;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;

	public MonedaEmpresa() {
		super();
		this.id = 0;
	}

	@Override
	public String toString() {
		return moneda.getNombre().toString();
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}else{
			if(!(obj instanceof MonedaEmpresa)){
				return false;
			}else{
				if(((MonedaEmpresa)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
		}
	}
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}	

}


