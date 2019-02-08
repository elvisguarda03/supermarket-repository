package br.com.guacom.supermarket.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.guacom.supermarket.DAO.CelularDAO;
import br.com.guacom.supermarket.DAO.ProdutoDAO;
import br.com.guacom.supermarket.DAO.ProdutoPerecivelDAO;
import br.com.guacom.supermarket.DAO.ProviderDAO;
import br.com.guacom.supermarket.internationalization.InternationalizationUtil;
import br.com.guacom.supermarket.model.ConnectionFactory;
import br.com.guacom.supermarket.model.Phone;
import br.com.guacom.supermarket.model.Produto;
import br.com.guacom.supermarket.model.ProdutoPerecivel;
import br.com.guacom.supermarket.model.Provider;

public class CadProdutosSupermercado {

	public static void main(String[] args) {
		int choose = 0;
		boolean restart = true;
		List<Produto> produtos = new ArrayList<Produto>();
		ProdutoDAO pDAO = null;
		ProdutoPerecivelDAO ppDAO = null;
		CelularDAO cDAO = null;
		ProviderDAO proDAO = null;
		try (Connection con = ConnectionFactory.getConnection()) {
			cDAO = new CelularDAO(con);
			proDAO = new ProviderDAO(con, cDAO);
			pDAO = new ProdutoDAO(con, cDAO, proDAO);
			ppDAO = new ProdutoPerecivelDAO(pDAO, con);

			while (restart) {
				choose = Integer.parseInt(JOptionPane.showInputDialog(InternationalizationUtil.getBundle("choose")
						+ "\n1 - " + InternationalizationUtil.getBundle("cad_prod") + "\n2 - "
						+ InternationalizationUtil.getBundle("cad_prod_p") + "\n3 - "
						+ InternationalizationUtil.getBundle("show") + "\n      "
						+ InternationalizationUtil.getBundle("comum") + "\n      "
						+ InternationalizationUtil.getBundle("perecivel") + "\n4 - "
						+ InternationalizationUtil.getBundle("clear_data") + "\n5 - "
						+ InternationalizationUtil.getBundle("exit")));
				produtos.clear();
				switch (choose) {
				case 1:
					cadastra(choose, produtos);
					try {
						pDAO.insert(produtos);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
					break;
				case 2:
					cadastra(choose, produtos);
					ppDAO.insert(produtos);
					break;
				case 3:
					choose = Integer.parseInt(JOptionPane.showInputDialog("1 - Comum\n2 - Perecível"));
					if (choose == 1) {
						try {
							produtos.addAll(pDAO.listar());
							for (Produto produto : produtos) {
								if (produto instanceof Produto)
									JOptionPane.showMessageDialog(null, produto + "\n" + produto.getProvider().getPhones());
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					} else {
						try {
							produtos = ppDAO.listar();
							for (Produto produto : produtos) {
								if (produto instanceof ProdutoPerecivel) {
									ProdutoPerecivel pp = (ProdutoPerecivel) produto;
									JOptionPane.showMessageDialog(null, pp + "\nData de validade: " + pp.getDateFormat());
								}
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					}
					break;
				case 4:
					break;
				case 5:
					System.exit(0);
					break;
				default:
					break;
				}
				restart = (JOptionPane.showConfirmDialog(null, "Deseja continuar?") == 0);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void cadastra(int escolha, List<Produto> produtos) {
		String nome = null, type = null, brand = null, dtManu = null, cnpj = null, nomeFornecedor = null,
				telefone = null, operadora = null;
		int codProd = 0;
		String dateVal = null;

		nome = JOptionPane.showInputDialog("Nome do produto:");
		type = JOptionPane.showInputDialog("Tipo");
		brand = JOptionPane.showInputDialog("Marca");
		dtManu = JOptionPane.showInputDialog("Data de fabricação");
		cnpj = JOptionPane.showInputDialog("cnpj");
		nomeFornecedor = JOptionPane.showInputDialog("Nome do fornecedor");

		Provider prov = new Provider(cnpj, nomeFornecedor);
		int choose = escolha;
		escolha = 0;
		while (escolha == 0) {
			telefone = JOptionPane.showInputDialog("Digite o número do telefone");
			operadora = JOptionPane.showInputDialog("Nome da operadora");
			prov.add(new Phone(telefone, operadora, prov));
			escolha = JOptionPane.showConfirmDialog(null, "Deseja continuar?");
		}
		if (choose == 2) {
			codProd = Integer.parseInt(JOptionPane.showInputDialog("Código do produto: "));
			int[] date = data(dateVal = JOptionPane.showInputDialog("Data de validade"));
			GregorianCalendar gc = new GregorianCalendar();
			gc.set(date[0], date[1], date[2]);
			java.util.Date da = gc.getGregorianChange();
			java.sql.Date d = new java.sql.Date(da.getYear(), da.getMonth(), da.getDay());
			prov.setProduto(new ProdutoPerecivel(nome, type, brand, prov, codProd, d));
			produtos.add(prov.getProduct());
		} else {
			Produto product = new Produto(nome, type, brand, prov);
			prov.setProduto(product);
			produtos.add(product);
		}
	}

	public static int[] data(String dt) {
		int[] data = new int[3];
		data[0] = Integer.parseInt((String) dt.subSequence(dt.lastIndexOf("/") + 1, dt.length()));
		int position = dt.length() - 8;
		data[1] = Integer.parseInt((String) dt.subSequence(position + 1, position + 2));
		position = 0;
		data[2] = Integer.parseInt((String) dt.subSequence(0, 2));
		return data;
	}
}