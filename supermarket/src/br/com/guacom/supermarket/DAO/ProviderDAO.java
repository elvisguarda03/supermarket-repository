package br.com.guacom.supermarket.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import br.com.guacom.supermarket.model.Provider;

public class ProviderDAO {
	private Connection connection;
	private CelularDAO cDAO;
	
	public ProviderDAO(Connection connection, CelularDAO cDAO) {
		this.connection = connection;
		this.cDAO = cDAO;
	}

	public boolean insert(Provider p) {

		return false;
	}

	public int execute(Provider p) {
		String sql = "insert into fornecedor (cnpj, nome) values (?,?)";
		loadData(p);
		try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			connection.setAutoCommit(false);
			stmt.setString(1, p.getCnpj());
			stmt.setString(2, p.getName());
			stmt.executeUpdate();
			connection.commit();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next())
					return Integer.parseInt(rs.getString("GENERATED_KEY"));
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}

	public int insertReturnKeyGenerated(Provider p) {
		return execute(p);
	}

	public boolean update(Provider p) {
		String sql = "update fornecedor set cod_produto = ? where id_fornecedor=?";
		try (PreparedStatement stmt = connection.prepareCall(sql)) {
			stmt.setInt(1, p.getProduct().getId());
			stmt.setInt(2, p.getId());
			stmt.executeUpdate();
			return stmt.getUpdateCount() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void loadData(Provider p) {
		String sql = "select f.nome, f.cnpj, c.* from fornecedor f inner join celular c on c.cod_provedor = f.id_fornecedor and f.cnpj = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, p.getCnpj());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					p.setId(rs.getInt("f.id_fornecedor"));
					p.setName(rs.getString("f.nome"));
					p.setPhones(cDAO.buscar(p.getId()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}