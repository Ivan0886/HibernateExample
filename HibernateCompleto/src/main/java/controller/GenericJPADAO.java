package controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class GenericJPADAO<T,K> implements DAOInterface<T,K>
{	
	private final static String PERSITENCEUNITNAME = "hibernate";	
	private Class<T> entityClass;
	private String persitenceUnitName;
	
	public GenericJPADAO(Class<T> entityClass) 
	{
		this.entityClass = entityClass;
		persitenceUnitName = PERSITENCEUNITNAME;
	}
	
	
	public GenericJPADAO(Class<T> entityClass, String persitenceUnitName) 
	{
		this.entityClass = entityClass;
		this.persitenceUnitName = persitenceUnitName;
	}

	
	@Override
	public Optional<T> findById(K key) 
	{
		EntityManagerFactory emFactory = EntityManagerFactorySingleton.getInstance(persitenceUnitName).getEmf();	
		EntityManager em = emFactory.createEntityManager();
		
		Optional<T> result = Optional.ofNullable(em.find(entityClass, key));
		
		em.close();
		
		return result;
	}

	
	@Override
	public Iterable<T> findAll() 
	{
		List<T> result;
		String jpaQuery;
		
		EntityManagerFactory emFactory = EntityManagerFactorySingleton.getInstance(persitenceUnitName).getEmf();	
		EntityManager em = emFactory.createEntityManager();

		jpaQuery = "SELECT o FROM " + entityClass.getSimpleName() + " o";
		Query query= em.createQuery(jpaQuery);
		
		result = query.getResultList();

		em.close();
		
		return result;
	}

	
	@Override
	public T delete(T ov) 
	{
		EntityManagerFactory emFactory = EntityManagerFactorySingleton.getInstance(persitenceUnitName).getEmf();	
		EntityManager em = emFactory.createEntityManager();	
		
		Object key = getKey(ov);
		
		if(key != null)
		{
			try 
			{
				em.getTransaction().begin();
				ov = em.find(entityClass, key);
				
				if(ov != null)
				{
					em.remove(ov);
				} else {
					ov = null;
				}
				
				em.getTransaction().commit();
			
			} catch(Exception e) {
				System.out.println(e.toString());
				ov = null;
			} finally {
				em.close();
			}
		} else {
			ov = null;
		}
		return ov;
	}

	
	@Override
	public T save(T ov) 
	{
		EntityManagerFactory emFactory = EntityManagerFactorySingleton.getInstance(persitenceUnitName).getEmf();	
		EntityManager em = emFactory.createEntityManager();	
		
		Object key = getKey(ov);
		
		if (key != null)
		{
			try 
			{
				em.getTransaction().begin();	
				
				if(em.find(entityClass, key) == null)
				{
					ov = em.merge(ov);
				} else {
					throw new EntityExistsException ();
				}
				em.getTransaction().commit();
			
			} catch(Exception e) {
				e.printStackTrace();
				ov = null;
			} finally {
				em.close();
			}
		} else {
			ov = null;
		}
		return ov;
	}

	
	@Override
	public T update(T ov) 
	{
		EntityManagerFactory emFactory = EntityManagerFactorySingleton.getInstance(persitenceUnitName).getEmf();	
		EntityManager em = emFactory.createEntityManager();	
		
		try 
		{
			em.getTransaction().begin();
		
			ov = em.merge(ov);
			
			em.getTransaction().commit();
		
		} catch(Exception e) {
			ov = null;
		} finally {
			em.close();
		}
		
		return ov;
	}
	
	
	private Object getKey(Object object)  
	{
		String nameGet;
		Object valor = null;
		boolean key;	
		
		// Comprobar si el atributo tiene una anotación Id
		Predicate<Field> isKey = f -> Arrays.stream(f.getAnnotations()).
										anyMatch(a ->a.annotationType().getSimpleName().contains("Id"));
		
		// Obtener atributo clave
		Optional<Field> field = Arrays.stream(entityClass.getDeclaredFields()).filter(isKey).findFirst();
		
		if(field.isPresent())
		{
			// Crear método get de la clave
			Field f = field.get();
			
			nameGet = "get" + f.getName().substring(0, 1).toUpperCase()+ f.getName().substring(1);

			
			// Obtener el valor de la clave
			try 
			{
				valor = entityClass.getDeclaredMethod(nameGet, null).invoke(object, null);

			} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				valor = null;
			}
		}		

		return valor;
	}
	
	
	public Stream executeQuery(String query, Object... params)
	{
		Stream result;
		EntityManagerFactory emFactory = EntityManagerFactorySingleton.getInstance(persitenceUnitName).getEmf();
		EntityManager em = emFactory.createEntityManager();
		
		Query q = em.createQuery(query);
		
		for(int i = 0; i < params.length; i++)
		{
			q.setParameter(i+1, params[i]);
		}
	
		if(isUpdateQuery(query))
		{
			try 
			{
				em.getTransaction().begin();
				result = Stream.of(q.executeUpdate());
				em.getTransaction().commit();
			} catch(Exception e) {
				result = Stream.empty();
			}	
		} else {
			result = q.getResultStream();
		}
		return result;
	}

	
	private boolean isUpdateQuery(String q) 
	{
		return !q.split(" ")[0].equalsIgnoreCase("select");
	}

	
	@Override
	public Stream executeQueryNamed(String nameQuery, Object... params) 
	{
		EntityManagerFactory emFactory = EntityManagerFactorySingleton.getInstance(persitenceUnitName).getEmf();	
		EntityManager em = emFactory.createEntityManager();
		
		Query q = em.createNamedQuery(nameQuery);
		
		for(int i = 0; i < params.length; i++) 
		{
			q.setParameter(i+1, params[i]);
		}
		
		return q.getResultStream();
	}
}