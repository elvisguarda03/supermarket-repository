package br.com.guacom.supermarket.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import br.com.guacom.supermarket.model.Produto;
import br.com.guacom.supermarket.model.ProdutoPerecivel;

public class ProdutoPerecivelDAO {
	private ProdutoDAO pDAO;
	private Connection connection;

	public ProdutoPerecivelDAO(ProdutoDAO pDAO, Connection connection) {
		this.pDAO = pDAO;
		this.connection = connection;
	}

	public boolean insert(Produto produto) {
		return execute(produto);
	}

	private boolean execute(Produto p) {
		String sql = "insert into produto_perecivel (cod_prod, code_produto, validade) values (?,?,?)";
		int id = pDAO.insertReturnKey(p);
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			ProdutoPerecivel pp = (ProdutoPerecivel) p;
			pstmt.setInt(1, id);
			pstmt.setInt(2, pp.getCodProd());
			pstmt.setDate(3, pp.getDtVal());
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insert(List<Produto> produtos) {
		try {
			connection.setAutoCommit(false);
			for (Produto p : produtos)
				execute(p);
			connection.commit();
			return true;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	public List<Produto> listar() {
		String sql = "select p.nome, p.id_produto, p.modelo, p.marca, f.nome, f.id_fornecedor, pp.code_produto, pp.validade from produto_perecivel pp \r\n"
				+ "inner join produto p on p.id_produto = pp.cod_prod inner join fornecedor f on f.id_fornecedor = p.cod_fornecedor group by pp.code_produto;";
		List<Produto> pps = new LinkedList<>();
		try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				pps.add(new ProdutoPerecivel(rs.getString("p.nome"), rs.getString("modelo"), rs.getString("marca"),
						rs.getInt("p.id_produto"), rs.getInt("pp.code_produto"), rs.getDate("validade")));
				pps.get(pps.size() - 1).setProvider(pDAO.buscar(pps.get(pps.size() - 1).getId()).getProvider());
			}
			return pps;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException("Não existem dados cadastrados!");
	}
}
