package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

@Entity
@DiscriminatorValue(value="Emp")
public class CuentaEmpresa extends Cuenta 
{
	@Column(length=24) private String cif;
	@Column(length=20) private String nombre;
	@Enumerated(EnumType.STRING) private Local local;

	
	@Override
	public boolean retirarDinero(float retirada) 
	{
		boolean exito = false;
		if(retirada > 0) 
		{
			if((getSaldo() - retirada) >= maximoNegativo()) 
			{
				setSaldo(getSaldo() - retirada);
				exito = true;
			}
		}
		
		return exito;
	}


	@Override
	public float maximoNegativo() 
	{
		return (totalAval() * 2) * -1;
	}
}