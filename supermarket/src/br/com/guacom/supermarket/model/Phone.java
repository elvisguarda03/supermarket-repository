package br.com.guacom.supermarket.model;

public class Phone implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2163730484922640899L;
	private Integer id;
	private String number;
	private String operator;
	private Provider provider;
	
	public Phone(String number, String operator, Provider provider, Integer id) {
		if( (number == null || operator == null || provider == null || id == null) )
			throw new NullPointerException("Campo vázio!");
		this.number = number;
		this.operator = operator;
		this.provider = provider;
		this.id = id;
	}
	
	
	public Phone(String number, String operator, Provider provider) {
		if( (number == null || operator == null || provider == null) )
			throw new NullPointerException("Campo vázio!");
		this.number = number;
		this.operator = operator;
		this.provider = provider;
	}
	
	public Phone(String number, String operator) {
		if(number == null || operator == null)
			throw new IllegalArgumentException();
		this.number = number;
		this.operator = operator;
	}
	
	public Phone(String number, Provider provider) {
		if( (number == null || provider == null) )
			throw new NullPointerException("Campo vázio!");
		this.number = number;
		this.provider = provider;
	}
	
	public Phone() {}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		if(number == null)
			throw new NullPointerException("Campo vázio!");
		this.number = number;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public void setOperator(String operator) {
		if(operator == null)
			throw new NullPointerException("Campo vázio!");
		this.operator = operator;
	}
	
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		if(provider == null)
			throw new IllegalArgumentException("Dados do fornecedor inválidos!");
		this.provider = provider;
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		if(id == null)
			throw new IllegalArgumentException();
		this.id = id;
	}
	
	@Override
	public String toString() {
		return String.format("Telefone - %s", number);
	}
}