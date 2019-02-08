package br.com.guacom.supermarket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Provider implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2622669445267901500L;
	private Integer id;
	private String cnpj;
	private String name;
	private Produto product;
	private List<Phone> phones;

	public Provider(String cnpj, String name) {
		if ((cnpj == null || name == null))
			throw new NullPointerException("Campo vázio!");
		this.cnpj = cnpj;
		this.name = name;
		this.phones = new ArrayList<>();
	}

	public Provider(String name, Produto product) {
		if (product == null || name == null)
			throw new NullPointerException("Campo inválido!");
		this.name = name;
		this.product = product;
		this.phones = new ArrayList<>();
	}

	public Provider() {
	}

	public Provider(String cnpj, String name, Integer id) {
		if (cnpj == null || name == null || id == null)
			throw new IllegalArgumentException("Dados não foram cadastrados corretamente!");
		this.cnpj = cnpj;
		this.name = name;
		this.id = id;
	}

	public Provider(String name, Produto product, Integer id) {
		if (name == null || product == null || id == null)
			throw new IllegalArgumentException("Dados não foram cadastrados corretamente!");
		this.name = name;
		this.product = product;
		this.id = id;
	}

	public String getCnpj() {
		return cnpj;
	}

	public String getName() {
		return name;
	}

	public Produto getProduct() {
		return product;
	}

	public List<Phone> getPhones() {
		return Collections.unmodifiableList(phones);
	}

	public boolean add(Phone phone) {
		if (phone == null)
			throw new IllegalArgumentException("Objeto não foi cadastrado!");
		return phones.add(phone);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			throw new NullPointerException("Objeto não foi cadastrado!");
		if (!(obj instanceof Provider))
			throw new IllegalArgumentException("Objeto não é um Provedor.");
		Provider prov = (Provider) obj;
		return this.cnpj.compareTo(prov.cnpj) == 0;
	}

	public void setProduto(Produto produto) {
		this.product = produto;
	}

	public int getId() {
		return id;
	}

	public void setPhones(List<Phone> asList) {
		this.phones = asList;
	}

	public void setId(Integer id) {
		if (id == null)
			throw new IllegalArgumentException();
		this.id = id;
	}

	public void setName(String name) {
		if(name == null)
			throw new IllegalArgumentException("Dados não foram cadastrados!");
		this.name = name;
	}
}