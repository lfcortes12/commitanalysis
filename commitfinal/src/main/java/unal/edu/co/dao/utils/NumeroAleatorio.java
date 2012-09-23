package unal.edu.co.dao.utils;
import java.util.ArrayList;
public class NumeroAleatorio {
    private int valorInicial;
    private int valorFinal;
    private ArrayList<Integer> listaNumero;

    public NumeroAleatorio(int valorInicial, int valorFinal){
        this.valorInicial = valorInicial;
        this.valorFinal = valorFinal;
        listaNumero = new ArrayList<Integer>();
    }
    
    private int numeroAleatorio(){
        return (int)(Math.random()*(valorFinal-valorInicial+1)+valorInicial);
    }
    
	public int generar() {
		if (listaNumero.size() < (valorFinal - valorInicial) + 1) {
			int numero = numeroAleatorio();// genero un numero
			if (listaNumero.isEmpty()) {// si la lista esta vacia
				listaNumero.add(numero);
				return numero;
			} else {// si no esta vacia
				if (listaNumero.contains(numero)) {
					return generar();
				} else {// Si no esta contenido en la lista
					listaNumero.add(numero);
					return numero;
				}
			}
		} else {// ya se generaron todos los numeros
			return -1;
		}
	}
}