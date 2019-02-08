package br.com.guacom.supermarket.model;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class ProdutoPerecivel extends Produto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1552729086389502640L;
	private int codProd;
	private Date dtVal;
	private String dateFormat;

	public ProdutoPerecivel(String name, String type, String brand, int id, int codProd, Date da) {
		super(name, type, brand, id);
		if (da == null)
			throw new NullPointerException("Objeto não foi cadastrado!");
		this.codProd = codProd;
		this.dtVal = da;
	}

	public ProdutoPerecivel(String nome, String type, String brand, Provider p, int codProd, Date d) {
		super(nome, type, brand, p);
		if (d == null)
			throw new IllegalArgumentException("Data inválida!");
		this.codProd = codProd;
		this.dtVal = d;
	}

	public int getCodProd() {
		return codProd;
	}

	public String getDateFormat() {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy").format(dtVal);
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		if (dateFormat == null)
			throw new IllegalArgumentException();
		this.dateFormat = dateFormat;
	}

	@Override
	public String typeProduct() {
		return "PP";
	}

	public Date getDtVal() {
		return Date.valueOf(new SimpleDateFormat("yyyy/MM/dd").format(dtVal));
	}
}
