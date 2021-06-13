package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
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
@DiscriminatorValue(value="Part")
public class CuentaParticular extends Cuenta 
{
	private boolean tarjetaCredito;
	
	@Override
	public boolean retirarDinero(float retirada) 
	{
		boolean exito = false;
		if(retirada > 0) 
		{
			if((getSaldo() - retirada) >= ((totalAval() / 2) * -1)) 
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
		return (totalAval() / 2) * -1;
	}
}