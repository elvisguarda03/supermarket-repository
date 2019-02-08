package br.com.guacom.supermarket.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import br.com.guacom.supermarket.model.Phone;
import br.com.guacom.supermarket.model.Produto;
import br.com.guacom.supermarket.model.Provider;

public class ProdutoDAO {
	protected Connection connection;
	protected CelularDAO cDAO;
	protected ProviderDAO proDAO;

	public ProdutoDAO(Connection connection, CelularDAO cDAO, ProviderDAO proDAO) {
		this.connection = connection;
		this.cDAO = cDAO;
		this.proDAO = proDAO;
	}

	public boolean insert(Produto p) {
		return execute(p) > -1;
	}

	private int execute(Produto p) {
		String sql = "insert into produto (nome, modelo, marca, cod_fornecedor, tipo_produto) values (?,?,?,?,?)";
		int idFornecedor = proDAO.insertReturnKeyGenerated(p.getProvider());
		p.getProvider().setId(idFornecedor);
		cDAO.insert(p.getProvider().getPhones());

		try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, p.getName());
			stmt.setString(2, p.getType());
			stmt.setString(3, p.getBrand());
			stmt.setInt(4, p.getProvider().getId());
			stmt.setString(5, p.typeProduct());
			stmt.executeUpdate();
			int idProduto = 0;
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					idProduto = Integer.parseInt(generatedKeys.getString("GENERATED_KEY"));
					return idProduto;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
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
			e.printStackTrace();
		}
		return false;
	}

	protected int insertReturnKey(Produto p) {
		return execute(p);
	}

	public List<Produto> listar() {
		String sql = "select p.nome, p.modelo, p.marca, p.id_produto, f.* from produto p inner join fornecedor f on f.id_fornecedor = p.cod_fornecedor and tipo_produto like 'P'";
		List<Produto> produtos = new LinkedList<>();
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					produtos.add(new Produto(rs.getString("p.nome"), rs.getString("modelo"), rs.getString("marca"),
							rs.getInt("id_produto")));
					int index = produtos.size() - 1;
					produtos.get(index).setProvider(new Provider(rs.getString("f.nome"), produtos.get(index), rs.getInt("id_fornecedor")));
					produtos.get(index).getProvider().setPhones(cDAO.buscar(produtos.get(index).getProvider().getId()));
				}
				return produtos;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new NoSuchElementException();
	}

	public Produto buscar(int codProduto) {
		String sql = "select p.nome, p.modelo, p.marca, p.id_produto, f.id_fornecedor, f.nome, f.cnpj from produto p inner join fornecedor f on p.cod_fornecedor = f.id_fornecedor "
				+ "and ? = p.id_produto";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, codProduto);
			ResultSet query = stmt.executeQuery();
			Produto p = null;
			while (query.next()) {
				p = new Produto(query.getString("nome"), query.getString("modelo"), query.getString("marca"),
						new Provider(query.getString("cnpj"), query.getString("nome"), query.getInt("f.id_fornecedor")), query.getInt("p.id_produto"));
				p.getProvider().setProduto(p);
				p.getProvider().setPhones(cDAO.buscar(p.getProvider().getId()));
				for (Phone c : p.getProvider().getPhones()) {
					try {
						c.setProvider(p.getProvider());
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			return p;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new NoSuchElementException();
	}

	public boolean update(Produto p) {
		String sql = "update produto set cod_fornecedor = ?, modelo = ?, marca = ? where id_produto = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, p.getProvider().getId());
			stmt.setString(2, p.getType());
			stmt.setString(3, p.getBrand());
			stmt.setInt(4, p.getId());
			stmt.executeUpdate();
			return stmt.getUpdateCount() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean delete(int codProduto) {
		String sql = "delete from produto where id=?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, codProduto);
			stmt.executeUpdate();
			return stmt.getUpdateCount() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}