package br.com.guacom.supermarket.DAO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import br.com.guacom.supermarket.model.Produto;
import br.com.guacom.supermarket.model.Provider;

public class PersistenceDAO {
	private static int sizeProducts = 0;
	private static int sizeProviders = 0;

	public void write(Provider provider) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(
				Files.newOutputStream(Paths.get("provider.bin"), StandardOpenOption.APPEND));
		oos.writeObject(provider);
		oos.writeInt(++sizeProviders);
	}

	public List<Provider> readProvider() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("provider.bin")));
		List<Provider> providers = new ArrayList<>();
		for (int i = 0; i < ois.readInt(); i++) {
			providers.add((Provider) ois.readObject());
		}
		if (providers.size() > 0)
			return providers;
		return null;
	}

	public void write(List<Produto> produtos) throws IOException {
		Path path = Paths.get("mercado.bin");
		if (Files.notExists(path))
			Files.createFile(path);
		ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path, StandardOpenOption.APPEND));
		
		oos.writeObject(produtos);
		oos.close();
	}

	public List<Produto> readProducts() throws IOException, ClassNotFoundException, NoSuchFileException {
		ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("mercado.bin")));
		List<Produto> produtos = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			produtos.add((Produto) ois.readObject());
		}
		ois.close();
		return produtos;
	}

	public Produto readProduct(Produto product) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("mercado.bin")));
		for (int i = 0; i < ois.readInt(); i++) {
			Produto p = (Produto) ois.readObject();
			if (p.equals(product)) {
				ois.close();
				return p;
			}
		}
		ois.close();
		return null;
	}

	public void setProvider(Provider provider) throws IOException {
		if (provider == null)
			throw new IllegalArgumentException("Objeto não foi cadastrado!");
		write(provider);
	}

	public void clear() throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(
				Files.newOutputStream(Paths.get("mercado.bin"), StandardOpenOption.DELETE_ON_CLOSE));
		oos.writeObject(null);
	}

	public void setProvider(List<Provider> providers) throws IOException {
		if (providers == null)
			throw new NullPointerException("Objeto não foi cadastrado!");
		for (Provider provider : providers)
			write(provider);
	}

	public Provider getProvider(Provider provider) throws ClassNotFoundException, IOException {
		List<Provider> providers = readProvider();
		for (Provider prov : providers) {
			if (prov.equals(provider))
				return prov;
		}
		return null;
	}

	public List<Provider> getProviders() throws ClassNotFoundException, IOException {
		return readProvider();
	}

	public List<Produto> getProdutos() throws ClassNotFoundException, IOException, NoSuchFileException {
		return readProducts();
	}

	public Produto getProduto(Produto p) throws ClassNotFoundException, IOException, NoSuchFileException {
		return readProduct(p);
	}

	public void setProdutos(List<Produto> produtos) throws IOException {
		if (produtos == null)
			throw new NullPointerException("Objeto não foi cadastrado!");
		write(produtos);
	}
}