/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Restriccion {
    public int id;
    public double x1, x2, signoCoeficiente;
    public String tipoF;
    public static final String menorOigual = "<=";
    public static final String mayorOigual = ">=";
    public static final String igual = "=";
    public static final double delta = 0.000001;

    public Restriccion(int id, double x1, double x2, double signoCoeficiente, String tipoF) {
        super();
        this.id = id;
        if((x1 == 0 && x2 == 0 ) ) {
			throw new IllegalArgumentException("Datos no válidos en la restricción "+id+".");
		}
        this.x1 = x1;
        this.x2 = x2;
        this.signoCoeficiente = signoCoeficiente;
        this.tipoF = tipoF;
    }
    

    public double evaluarPunto(PuntoSolucion p) {
        return (p.x*x1)+(p.y*x2);
    }
    
    public double valor0Y(double x)throws Exception{
        if(this.x1==0){
		throw new Exception("Dado que el valor del coeficiente x2 es 0. No se puede calcular la función. ");
        }
        else{
            return (this.signoCoeficiente-(this.x1*x))/this.x2;
        }    
    }
    
    public boolean realizacionFunc(PuntoSolucion p) {
        double evaluarPunto2 = evaluarPunto(p);
	if(this.tipoF.equals(menorOigual)) {
            return (evaluarPunto2 <= this.signoCoeficiente + delta);
	}
        else if(this.tipoF.equals(mayorOigual)) {
            return (evaluarPunto2 >= this.signoCoeficiente - delta);
	}else{
            return (evaluarPunto2 >= this.signoCoeficiente -delta && evaluarPunto2<=this.signoCoeficiente + delta);
	}
    }
    
    public ArrayList<PuntoSolucion> intersecciónDeLosPuntos(){
        ArrayList<PuntoSolucion> segmento = new ArrayList<PuntoSolucion>();
	double s =this.signoCoeficiente/this.x2;
        double s2 =this.signoCoeficiente/this.x1;
               
	if(!Double.isInfinite(s) && !Double.isNaN(s)){
            segmento.add(new PuntoSolucion(0,s));
        }    
	if(!Double.isInfinite(s2) && !Double.isNaN(s2)){
            segmento.add(new PuntoSolucion(s2,0));
	}
		
	return segmento;
    }
    
    public PuntoSolucion intersecionGeneral(Restriccion r) {
        double segmentoDividido =(this.signoCoeficiente * r.x2) - (r.signoCoeficiente * this.x2);
        double segmentoDivididoAb=(this.x1 * r.x2) - (r.x1 * this.x2);
        double x = segmentoDividido/segmentoDivididoAb;
        double y;
                
        if(this.x2 != 0){
            y =(this.signoCoeficiente-(this.x1 * x))/this.x2;
        }
        else{
            y =(r.signoCoeficiente -(r.x1 *x))/r.x2;
        }

        if(!Double.isNaN(x)  && !Double.isNaN(y)) {
            return new PuntoSolucion(x,y);
	}
        else{
            return null;
	}	
    }
    
    @Override
    public String toString() {
	String espacio = "";
	if(this.x2 >= 0) {
            espacio = "+";
	}
	return this.x1+ "X1" +espacio +this.x2 + "X2" +this.tipoF+ this.signoCoeficiente;
    }

    
    
    
   
}
