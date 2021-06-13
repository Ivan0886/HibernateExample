package model;

import java.util.HashSet;
import java.util.stream.Stream;
import controller.GenericJPADAO;

public class App 
{
	static GenericJPADAO<Cuenta, String> cuentaDAO = new GenericJPADAO(Cuenta.class, "model");
	static GenericJPADAO<Cliente, String> clienteDAO = new GenericJPADAO(Cliente.class, "model");
	
	public static void main(String[] args) 
	{	
		String query;
		Stream<Object[]> streamArray;
		
		HashSet<Cuenta> cuentas = cargarDatos();
		
		cuentas.forEach(cuentaDAO :: save);
		
		/*Ingresar dinero. La aplicación solicitará al usuario el nº de cuenta y la cantidad a
		ingresar. Una vez realizado el ingreso, mostrará el saldo actual.*/
		Cuenta cuenta = cuentaDAO.findById("111111111").orElseGet(CuentaParticular::new);
		System.out.println(cuenta.ingresarDinero(2000) ? "saldo " + cuenta.getSaldo() : "No existe la cuenta");
		
		/*Retirar dinero. La aplicación solicitará al usuario el nº de cuenta y la cantidad a
		retirar. Una vez realizado el reintegro, mostrará el saldo actual, si ha sido
		posible la operación, en caso contrario, mostrará por pantalla un mensaje
		indicando la cantidad máxima a retirar*/
		cuenta = cuentaDAO.findById("111111111").orElseGet(CuentaParticular::new);
		System.out.println(cuenta.retirarDinero(2000) ? "saldo " + cuenta.getSaldo() : "El importe que puedes retirar maximo en esta cuenta es: " + ((cuenta.maximoNegativo() - cuenta.getSaldo()) * 1));
		
		/*Consultar saldo de una cuenta. La aplicación solicitará el nº de cuenta y
		mostrará todos los datos de la cuenta, incluido el saldo, así como la fecha y
		hora actuales del sistema con el formato como el mostrado “09/03/2012 12:40”.*/
		query = "SELECT c FROM Cuentas c WHERE c.nCuenta = ?1";
		cuentaDAO.executeQuery(query, "111111111").forEach(System.out::println);
		
		/*Consultar saldo de la entidad financiera. La aplicación mostrará por pantalla el nº
		de cuenta y el saldo de cada una de las cuentas y finalmente el total de la
		entidad, es decir la suma de todos los saldos de todas las cuentas.*/
		query = "SELECT c.nCuenta, c.saldo FROM Cuentas c";
		streamArray = cuentaDAO.executeQuery(query);
		streamArray.forEach(o->System.out.println(o[0] +","+o[1]));
		
		/*Consultar los datos del cliente que tenga más dinero en el banco, es decir el
		cliente que sumando el saldo de las cuentas en las que participa tenga un valor
		máximo.*/
		query = "SELECT c FROM Cliente c";
		streamArray = clienteDAO.executeQuery(query);
		
		/*Consultar la compañía de telecomunicaciones que más clientes tiene en la
		sucursal.*/
		
		/*Listar las cuentas empresariales con saldo negativo ordenadas de menor a
		mayor, es decir, primero las que más saldo negativo tengan.*/
	}
	
	
	public static boolean transferirDinero(float transferencia, Cuenta destino, Cuenta origen) 
	{
		boolean exito = false;
		float comision = (origen instanceof CuentaEmpresa) ? 0.4f : 0.2f;
		
		if(origen.retirarDinero((transferencia + (transferencia * comision) / 100))) 
		{
			destino.ingresarDinero(transferencia);
			cuentaDAO.update(origen);
			cuentaDAO.update(destino);
			exito = true;
		}
		return exito;
	}
	
	
	public static HashSet<Cuenta> cargarDatos() 
	{
		HashSet<Cuenta> cuentas = new HashSet<>();
		// Telefonos
		Telefono t1 = Telefono.builder().
				telefonoPK(TelefonoPK.builder().
					numero("912344354").
					company("Vodafone").
					build()).
				build();
				
		Telefono t2 = Telefono.builder().
				telefonoPK(TelefonoPK.builder().
					numero("917779889").
					company("Movistar").
					build()).
				build();
				
		Telefono t3 = Telefono.builder().
				telefonoPK(TelefonoPK.builder().
					numero("916657090").
					company("Orange").
					build()).
				build();
				
		// Clientes
		Cliente c1 = Cliente.builder().
				nif("00000001A").
				nombreCliente("Pepe").
				aval(25000).	
				telefono(t1).
				telefono(t2).
				build();
				
		Cliente c2 = Cliente.builder().
				nif("00000002B").
				nombreCliente("Javi").
				aval(50000).	
				telefono(t3).
				build();
		
		// Cuentas
		cuentas.add(CuentaEmpresa.builder().
				nCuenta("111111111").
				saldo(20000).
				cif("09876").
				nombre("PepeSA").
				local(Local.PROPIO).
				cliente(c1).
				build());
		
		cuentas.add(CuentaParticular.builder().
				nCuenta("222222222").
				saldo(5000).
				tarjetaCredito(true).
				cliente(c2).
				build());
		
		return cuentas;
	}
}