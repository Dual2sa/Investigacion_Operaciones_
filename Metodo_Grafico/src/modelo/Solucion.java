
package modelo;

import java.util.ArrayList;
import javax.swing.JTextArea;

public class Solucion {
    public double x1, x2;
    public boolean valorNegativo;
    public String tipoF;
    public ArrayList<Restriccion> restriccion;
    public static final String maximizar = "MAX";
    public static final String minimizar = "MIN";
      private JTextArea txtAreaResultados;

    public Solucion(double x1, double x2, boolean valorNegativo, String tipoF,JTextArea txtAreaResultados) {
        super();
        this.x1 = x1;
        this.x2 = x2;
        this.valorNegativo = valorNegativo;
        this.tipoF = tipoF;
        this.restriccion = new ArrayList<Restriccion>();
         this.txtAreaResultados = txtAreaResultados;
    }
        public void mostrarEnTxtArea(String mensaje) {
        txtAreaResultados.append(mensaje + "\n");
    }

    public void adicionarRestriccion(Restriccion r) {
	this.restriccion.add(r);
    }
    
    public double evaluandoObj(PuntoSolucion p) {
	return this.x1 * p.x + this.x2 *p.y;
    }
    
    public ArrayList<PuntoSolucion>realizarIntersección(){
        ArrayList<PuntoSolucion>puntos = new ArrayList<>();
        for (int i = 0; i < restriccion.size(); i++) {
            puntos.addAll(restriccion.get(i).intersecciónDeLosPuntos());
	}
        return puntos;
    }
    
    public ArrayList<PuntoSolucion>posiblesPuntos(){
	ArrayList<PuntoSolucion>puntos=new ArrayList<PuntoSolucion>();
	puntos.add(new PuntoSolucion(0,0));
	for (int i = 0; i < restriccion.size(); i++) {
            puntos.addAll(restriccion.get(i).intersecciónDeLosPuntos());
	}
		
	for (int i = 0; i < restriccion.size(); i++) {
            for (int j = i+1; j < restriccion.size(); j++) {
		PuntoSolucion p= restriccion.get(i).intersecionGeneral(restriccion.get(j));
		if(p!=null) {
                    puntos.add(p);
		}
                else{
                   mostrarEnTxtArea("Interseccion nula");
                }
            }
	}
		
	if(!this.valorNegativo) {
            for (int i = 0; i < puntos.size(); i++) {
		if(puntos.get(i).x<0 || puntos.get(i).y<0) {
                    puntos.remove(i);
                    i--;
		}
            }
	}
		
	for (int i = 0; i < puntos.size(); i++) {
            boolean posibilidad = true;
            for (int j = 0; j < restriccion.size() && posibilidad; j++) {
		if(!restriccion.get(j).realizacionFunc(puntos.get(i))) {
                    posibilidad = false;
                   mostrarEnTxtArea("Se ha descartado el punto " + puntos.get(i).toString() + " por la restricción: " + restriccion.get(j).toString());
                    puntos.remove(i);
                    i--;
		}
            }
	}
	 mostrarEnTxtArea("Posibilidad de puntos:");
	for (int i = 0; i < puntos.size(); i++) {
             mostrarEnTxtArea(puntos.get(i).toString());
	}
	return puntos;
    }
    
    public PuntoSolucion puntoIdeal() {
	ArrayList<PuntoSolucion>posibles_puntos = this.posiblesPuntos();
	PuntoSolucion op = null;
	if(this.tipoF.equals(this.maximizar)) {
             mostrarEnTxtArea("Máximo:");
            double max = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < posibles_puntos.size(); i++) {
		if(evaluandoObj(posibles_puntos.get(i))>max) {
                    max = evaluandoObj(posibles_puntos.get(i));
                    op = posibles_puntos.get(i);
		}
            }	
	}
        else {
	     mostrarEnTxtArea("Minimo:");
	    double min = Double.POSITIVE_INFINITY;
            for (int i = 0; i < posibles_puntos.size(); i++) {
		if(evaluandoObj(posibles_puntos.get(i))<min) {
                    min = evaluandoObj(posibles_puntos.get(i));
                    op=posibles_puntos.get(i);
		}
            }
	}
	return op;
    }
    
    
    @Override
    public String toString() {
	String t="";
	String espacio = "";
	if(this.x2 >= 0) {
            espacio = "+";
	}
	if(this.tipoF.equals(this.maximizar)) {
            t = "MAX";
	}
        else {
            t="MIN";
	}
	String st = t +" " + this.x1 + "X1" + espacio + "" + this.x2 +"X2 SUJETO A:"+"\n";
	if(!this.valorNegativo) {
            st +="X1,X2 >= 0" + "\n";
	}
	for (int i = 0; i < restriccion.size(); i++) {
            st += restriccion.get(i).toString()+"\n";
	}
	return st;
    }
	
    
    
    
}
