package model;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "TELEFONOS")
public class Telefono implements Serializable 
{
	@EqualsAndHashCode.Include
	@EmbeddedId
	private TelefonoPK telefonoPK;
	
	/*
	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	//@JoinColumn(name="cliente_identificador")
	private Cliente cliente;
	*/
}