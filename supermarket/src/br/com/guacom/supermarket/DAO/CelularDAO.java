package br.com.guacom.supermarket.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import br.com.guacom.supermarket.model.Phone;

public class CelularDAO {
	private Connection connection;

	public CelularDAO(Connection connection) {
		this.connection = connection;
	}

	public boolean insert(Phone p) {

		return false;
	}

	protected boolean insert(List<Phone> phones) {
		try {
			connection.setAutoCommit(false);
			for (Phone p : phones)
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
		throw new IllegalArgumentException();
	}

	private void execute(Phone p) throws SQLException {
		String sql = "insert into celular (numero, operadora, cod_provedor) values (?,?,?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, p.getNumber());
			stmt.setString(2, p.getOperator());
			stmt.setInt(3, p.getProvider().getId());
			stmt.executeUpdate();
		}
	}

	protected List<Phone> buscar(int codFornecedor) {
		String sql = "select * from celular where id_celular=?";
		List<Phone> phones = new ArrayList<>();
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, codFornecedor);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next())
					phones.add(new Phone(rs.getString("numero"), rs.getString("operadora")));
			}
			return phones;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new NoSuchElementException();
	}
}