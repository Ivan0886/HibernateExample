package controller;

import java.util.Optional;
import java.util.stream.Stream;

public interface DAOInterface <T,K> {
	
	public Optional<T> findById(K key);
	
	public  Iterable<T> findAll();
	
	public T delete(T ov);
	
	public T save(T ov);
	
	public T update(T ov);
	
	public Stream executeQuery (String query, Object... params);	
	
	public Stream executeQueryNamed (String nameQuery, Object... params);
}