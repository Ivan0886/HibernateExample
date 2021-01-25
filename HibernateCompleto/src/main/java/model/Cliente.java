package model;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import model.Telefono.TelefonoBuilder;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="CLIENTES")
public class Cliente 
{
	@EqualsAndHashCode.Include
	@Id
	@Column(length=9)
	private String nif;
	
	@Column(length=20)
	private String nombreCliente;
	
	private float aval;
	
	@Singular
	@ManyToMany(mappedBy = "clientes",fetch=FetchType.EAGER) // Relación perezosa por defecto
	private Set<Cuenta> cuentas;
	
	@Singular
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="IDCLIENTE_FK")
	private Set<Telefono> telefonos;
	
	@Override
	public String toString() {
		return "Cliente [nif=" + nif + ", nombreCliente=" + nombreCliente + ", aval=" + aval + ", telefonos=" + telefonos
				+ ", cuentas=" + cuentas.stream().map(Cuenta::getNCuenta).collect(Collectors.joining(",")) + "]";
	}
}
