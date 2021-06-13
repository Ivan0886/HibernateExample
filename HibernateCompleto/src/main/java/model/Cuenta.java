package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import java.util.Set;
import java.util.stream.Collectors;

@SuperBuilder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@NoArgsConstructor
@AllArgsConstructor

@Entity
@DiscriminatorColumn(name="tipo")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table (name="CUENTAS")
public abstract class Cuenta 
{
	@EqualsAndHashCode.Include
    @Id
    @Column(length=24)
	private String nCuenta;

	private float saldo;

	@Singular
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	// Tabla relación cuentas clientes
	@JoinTable(
			name = "REL_CUENTAS_CLIENTES",
		    joinColumns = {@JoinColumn(name = "FK_CUENTA", nullable = false)},
	        inverseJoinColumns = {@JoinColumn(name = "FK_CLIENTE", nullable = false)}
	)
	
	
	private Set<Cliente> clientes;
	
	
	public abstract boolean retirarDinero(float retirada);
	
	
	public abstract float maximoNegativo();
	
	
	public boolean ingresarDinero(float ingreso) 
	{
		boolean exito = false;
		
		if(ingreso > 0) 
		{
			this.saldo += ingreso;
			exito = true;
		}
		
		return exito;
	}
	
	
	public float totalAval() 
	{
		return (float)clientes.stream().mapToDouble(Cliente :: getAval).sum();
	}
	
	
	@Override
	public String toString() 
	{
		return "Conductor [nCuenta=" + nCuenta + ", saldo=" + saldo + ", "
				+ "clientes=" + clientes.stream().map(c -> c.getNif()).collect(Collectors.joining(",")) + "]";
	}
}