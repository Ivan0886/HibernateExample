package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.InheritanceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
	// Tabla relación clientes cuentas
		@JoinTable(
				name = "REL_CUENTAS_CLIENTES",
		        joinColumns = {@JoinColumn(name = "FK_CUENTA", nullable = false)},
		        inverseJoinColumns = {@JoinColumn(name = "FK_CLIENTE", nullable = false)}
			)
	private Set<Cliente> clientes;
	
	@Override
	public String toString() 
	{
		return "Conductor [nCuenta=" + nCuenta + ", saldo=" + saldo + ", "
				+ "clientes=" + clientes.stream().map(c -> c.getNif()).collect(Collectors.joining(",")) + "]";
	}
}
