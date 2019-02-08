package br.com.guacom.supermarket.model;

import java.util.Locale;

public class Produto implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5876646818983896987L;
	private Integer id;
	private String name;
	private String type;
	private String brand;
//	private Date dtManu;
	private Provider provider;
	
	public Produto(String name, String type, String brand, Provider provider, Integer id) {
		if(name == null || type == null || brand == null || provider == null || id == null)
			throw new NullPointerException("Objeto não foi cadastrado!");
		this.name = name;
		this.type = type;
		this.brand = brand;
		this.id = id;
//		this.dtManu = dtManu;
		this.provider = provider;
	}

	public Produto(String name, String type, String brand, Provider provider) {
		if(name == null || type == null || brand == null || provider == null)
			throw new NullPointerException("Objeto não foi cadastrado!");
		this.name = name;
		this.type = type;
		this.brand = brand;
//		this.dtManu = dtManu;
		this.provider = provider;
	}
	
	public Produto(String name, String type, String brand, Integer id) {
		if(name == null || type == null || brand == null || id == null)
			throw new NullPointerException("Objeto não foi cadastrado!");
		this.name = name;
		this.type = type;
		this.brand = brand;
		this.id = id;
//		this.dtManu = dtManu;
	}

	
	public Produto(String name, String brand, Provider provider) {
		if(name == null || brand == null || provider == null)
			throw new NullPointerException("Objeto não foi cadastrado!");
		this.name = name;
		this.brand = brand;
//		this.dtManu = dtManu;
		this.provider = provider;
	}
	
	public Produto(String name, String type, String brand) {
		
	}
	
	public Produto() {}

	public String getName() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getBrand() {
		return brand;
	}

//	public Date getDtManu() {
//		return dtManu;
//	}
	
	public void setProvider(Provider p) {
		if(p == null)
			throw new IllegalArgumentException();
		this.provider = p;
	}

	public Provider getProvider() {
		return provider;
	}
	
	public String typeProduct() {
		return "P";
	}
	
	@Override
	public String toString() {
		return String.format(Locale.US, "Nome do produto - %s%nMarca do produto - %s%nNome do fornecedor - %s%nCódigo: %d", 
				name, brand, provider.getName(), provider.getId());
	}

	public void setId(Integer id) {
		if(id == null)
			throw new IllegalArgumentException();
		this.id = id;
	}
}