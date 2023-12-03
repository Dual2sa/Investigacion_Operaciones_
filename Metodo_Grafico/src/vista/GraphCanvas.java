/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;


import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import modelo.*;

public class GraphCanvas extends Canvas{
    public Solucion cs;

    public GraphCanvas() {
        super();
    }

    public void traerSolucion(Solucion cs){
        this.cs = cs;
    }
    
    public void ejesConNegativos(Graphics g){
        int enW=this.getWidth();
        int enH=this.getHeight();
        int puntoX=enW/2;
        int puntoY=enH/2;
        int tamano=enH-20;
        
        g.setColor(Color.black);
        g.setFont(new Font("Tahoma",Font.BOLD,11));
        g.drawLine(10, enH/2, enW-10, enH/2);
        g.drawString("X", enW-10 + 2, enH/2 + 4);
        g.drawLine(enW/2, 10, enW/2, enH-10);
        g.drawString("Y", enW/2 - 2, 10 - 1);
            
        for (int i = -10; i <= 10; i++) {
            g.drawLine(puntoX+i*(tamano/20), puntoY-2, puntoX+i*(tamano/20), puntoY+2);
        }
        for (int i = -10; i <= 10; i++) {
            g.drawLine(puntoX-2,puntoY-i*(tamano/20), puntoX+2, puntoY-i*(tamano/20));
        }
    }
    
    public void ejesSinNegativos(Graphics g){
        int w=this.getWidth();
        int h=this.getHeight();
        int axisSize=h-20;
        int originX=10;
        int originY=h-10;
        
        g.setColor(Color.black);
        g.setFont(new Font("Tahoma",Font.BOLD,11));
        g.drawLine(10, 10, 10, h-10);
        g.drawLine(10, h-10, w-10, h-10);
        g.drawString("Y", 10-2, 10-1);
        g.drawString("X", w-10 + 2, h-10 + 4);
            
        for (int i = 1; i <= 10; i++) {
            g.drawLine(8, originY-i*(axisSize/10), 12,originY- i*(axisSize/10));
        }
            
        for (int i = 1; i <= 10; i++) {
            g.drawLine(originX+i*(axisSize/10), h-8, originX+i*(axisSize/10),h-12);
        }
    }
    
    public void numerosNegativos(Graphics g, double maxX, double maxY){
        int w=this.getWidth();
        int h=this.getHeight();
        int axisSize=h-20;
            
        g.setFont(new Font("Tahoma",Font.PLAIN,9));
        for(int i=0; i<=20; i++){
            if(i!=10){
                if(i%2==0){
                    g.drawString(String.format("%.2f", -maxX + i*(maxX/10)), 5+i*(axisSize/20)-1, h/2 -5);
                }
                else{
                    g.drawString(String.format("%.2f", -maxX + i*(maxX/10)), 5+i*(axisSize/20)-1, h/2 +12);
                }
            }   
        }
        
        for(int i=0; i<=20; i++){
            if(i!=10){
                g.drawString(String.format("%.2f", maxY - i*(maxY/10)),w/2+5, 10+i*(axisSize/20)+3);
            }   
        }
    }
    
    public void numerosNoNegativos(Graphics g, double maxX, double maxY){
        int w=this.getWidth();
        int h=this.getHeight();
        int axisSize=h-20;
        int originX=10;
        int originY=h-10;
        g.setFont(new Font("Tahoma",Font.PLAIN,9));
        for (int i = 1; i <= 10; i++) {
            g.drawString(String.format("%.2f", i*(maxX/10)), originX+i*(axisSize/10)-7, originY-5);
            g.drawString(String.format("%.2f", i*(maxY/10)),  originX+5,originY-i*(axisSize/10)+3);
        }
    }
    
    public PuntoSolucion puntoNegativo(PuntoSolucion p, double maxX, double maxY){
        int w=this.getWidth();
        int h=this.getHeight();
        //Transform X
        double xt=( ((w-20)/(2*maxX))*p.x  )+((w-20)/2)+10;
        double yt=(((h-20)/(-2*maxY))*p.y)+((h-20)/2)+10;
        return new PuntoSolucion(xt,yt);    
    }
    
    public void trazarPlano(Graphics g){
        Graphics2D g2d=(Graphics2D)g.create();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 500, 10);
        g2d.fillRect(this.getWidth()-10, 0, 10, 500);
        g2d.fillRect(0, this.getHeight()-10, 500, 10);
        g2d.fillRect(0, 0, 10, 500);
    }
    
    public void restriccionNegativa(Graphics g, double maxX, double maxY){
        ArrayList<Restriccion>restriccion = cs.restriccion;
        Graphics2D g2d=(Graphics2D)g.create();
        Stroke s=new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(s);

        int cont=0;
        for(Restriccion r:restriccion){
            if(r.tipoF.equals(Restriccion.menorOigual) || r.tipoF.equals(Restriccion.mayorOigual) ){
                cont++;
            }
        }
        if(cont == 0){
            cont = 1;
        }
        
        Color drawColor=new Color(0,0,0,0/cont);
        
        for(Restriccion r:restriccion){
            ArrayList<PuntoSolucion>puntos = r.intersecciónDeLosPuntos();
            g2d.setColor(drawColor);
            if(puntos .size()>0){
                if(puntos .size()==1){
                    if(r.x2 ==0){
                       PuntoSolucion p1=new PuntoSolucion(r.signoCoeficiente/r.x1, maxY); 
                       PuntoSolucion p2 = new PuntoSolucion(r.signoCoeficiente/r.x1,-maxY); 
                       p1 = this.puntoNegativo(p1, maxX, maxY);
                       p2 = this.puntoNegativo(p2, maxX, maxY);
                       
                       if(r.tipoF.equals(Restriccion.menorOigual)){
                            int[]xs={10,(int)p1.x,(int)p2.x,10};
                            int[]ys={10,10,this.getHeight()-10,this.getHeight()-10};
                            g2d.fillPolygon(xs, ys, 4);
                        }
                        else if(r.tipoF.equals(Restriccion.mayorOigual)){
                           int[]xs={(int)p1.x,this.getWidth()-10,this.getWidth()-10,(int)p2.x};
                          int[]ys={10,10,this.getHeight()-10,this.getHeight()-10};
                          g2d.fillPolygon(xs, ys,4);
                        }
                        g2d.setColor(Color.black);
                        g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);  
                    }
                    else{
                        try{
                            PuntoSolucion p1=new PuntoSolucion(-maxX,r.valor0Y(-maxX)); 
                            PuntoSolucion p2 = new PuntoSolucion(maxX,r.valor0Y(maxX)); 
                            p1 = this.puntoNegativo(p1, maxX, maxY);
                            p2 = this.puntoNegativo(p2, maxX, maxY);
                            
                            if(r.tipoF.equals(Restriccion.menorOigual)){
                                if(r.x2>0){
                                int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                int[]ys={(int)p1.y,(int)p2.y,this.getHeight()-10,this.getHeight()-10};
                                g2d.fillPolygon(xs, ys,4);
                            }
                            else{
                                int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                int[]ys={(int)p1.y,(int)p2.y,10,10};
                                g2d.fillPolygon(xs, ys,4);
                            }  
                            }else if(r.tipoF.equals(Restriccion.mayorOigual)){
                                if(r.x2>0){
                                    int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                    int[]ys={(int)p1.y,(int)p2.y,10,10};
                                    g2d.fillPolygon(xs, ys,4);
                                }
                                else{
                                    int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                    int[]ys={(int)p1.y,(int)p2.y,this.getHeight()-10,this.getHeight()-10};
                                    g2d.fillPolygon(xs, ys,4);
                                }    
                            }
                            g2d.setColor(Color.black);
                            g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                        }catch(Exception e){
                            
                        }  
                    }  
                }
                else{
                    try{
                        PuntoSolucion p1=new PuntoSolucion(-maxX,r.valor0Y(-maxX)); 
                        PuntoSolucion p2 = new PuntoSolucion(maxX,r.valor0Y(maxX)); 
                        p1 = this.puntoNegativo(p1, maxX, maxY);
                        p2 =this.puntoNegativo(p2, maxX, maxY);
                        if(r.tipoF.equals(Restriccion.menorOigual)){
                            if(r.x2>0){
                                int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                int[]ys={(int)p1.y,(int)p2.y,this.getHeight()-10,this.getHeight()-10};
                                g2d.fillPolygon(xs, ys,4);
                            }
                            else{
                                int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                int[]ys={(int)p1.y,(int)p2.y,10,10};
                                g2d.fillPolygon(xs, ys,4);
                            }  
                        }
                        else if(r.tipoF.equals(Restriccion.mayorOigual)){
                            if(r.x2>0){
                                int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                int[]ys={(int)p1.y,(int)p2.y,10,10};
                                g2d.fillPolygon(xs, ys,4);
                            }
                            else{
                                int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                int[]ys={(int)p1.y,(int)p2.y,this.getHeight()-10,this.getHeight()-10};
                                g2d.fillPolygon(xs, ys,4);
                            }     
                        }
                        g2d.setColor(Color.black);
                        g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                    }catch(Exception e){
                            
                     }   
                }
            }
        }
    }
    public PuntoSolucion puntoNoNegativo(PuntoSolucion p, double maxX, double maxY){
        int w = this.getWidth();
        int h =this.getHeight();
        double xt=(((20-w)/(-maxX))*p.x)+10;
        double yt=(((h-20)/(-maxY))*p.y)+h-10;
        return new PuntoSolucion(xt,yt);
    }
    
    public void planoNoNegativo(Graphics g, double maxX, double maxY){
        Graphics2D g2d=(Graphics2D)g.create();
        ArrayList<Restriccion>restriccion = cs.restriccion;
        Stroke s=new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(s);
        for(Restriccion r:restriccion){
            ArrayList<PuntoSolucion>puntos= r.intersecciónDeLosPuntos();
           
            if(puntos.size()>0){
                if(puntos.size()==1){
                    if(r.x2==0){
                       PuntoSolucion p1 = new PuntoSolucion(r.signoCoeficiente/r.x1,maxY); 
                       PuntoSolucion p2 = new PuntoSolucion(r.signoCoeficiente/r.x1,-maxY); 
                       p1 = this.puntoNoNegativo(p1, maxX, maxY);
                       p2=this.puntoNoNegativo(p2, maxX, maxY);
                       
                       g2d.setColor(Color.black);
                        if(r.tipoF.equals(Restriccion.igual)){
                           g2d.setColor(Color.red);
                       }
                       g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                       
                    }else{
                        try{
                            PuntoSolucion p1 = new PuntoSolucion(-maxX,r.valor0Y(-maxX)); 
                            PuntoSolucion p2 = new PuntoSolucion(maxX,r.valor0Y(maxX)); 
                            p1 = this.puntoNoNegativo(p1, maxX, maxY);
                            p2=this.puntoNegativo(p2, maxX, maxY);
                    
                            g2d.setColor(Color.black);
                            if(r.tipoF.equals(Restriccion.igual)){
                                g2d.setColor(Color.red);
                            }
                            g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                        }catch(Exception e){   
                        }
                    }    
                }else{
                    try{
                        PuntoSolucion p1=new PuntoSolucion(-maxX,r.valor0Y(-maxX)); 
                        PuntoSolucion p2=new PuntoSolucion(maxX,r.valor0Y(maxX)); 
                        p1 = this.puntoNoNegativo(p1, maxX, maxY);
                        p2 = this.puntoNoNegativo(p2, maxX, maxY);
                        
                        g2d.setColor(Color.black);
                        if(r.tipoF.equals(Restriccion.igual)){
                                                 g2d.setColor(Color.black);  g2d.setColor(Color.red);
                        }
                        g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                    }catch(Exception e){
                            
                     }   
                }
            }
        }
    }
    
        public void TPuntoPosible(Graphics g,double maxX, double maxY){
        Graphics2D g2d=(Graphics2D)g.create();
        ArrayList<PuntoSolucion>puntos = cs.posiblesPuntos();
        for (int i = 0; i < puntos .size(); i++) {
            if(cs.valorNegativo){
                PuntoSolucion p = this.puntoNegativo(puntos .get(i), maxX, maxY);
                g2d.setColor(Color.YELLOW);
                g2d.fillOval((int)p.x-2, (int)p.y-2, 4, 4);
                g2d.setColor(Color.black);
                g2d.drawOval((int)p.x-2, (int)p.y-2, 4, 4);
            }
            else{
                PuntoSolucion p=this.puntoNegativo(puntos .get(i), maxX, maxY);
                g2d.setColor(Color.YELLOW);
                g2d.fillOval((int)p.x-2, (int)p.y-2, 4, 4);
                g2d.setColor(Color.black);
                g2d.drawOval((int)p.x-2, (int)p.y-2, 4, 4);
            }
            
        }
    }
    public void lineaNegativa(Graphics g, double maxX, double maxY){
        ArrayList<Restriccion>restriccion = cs.restriccion;  
        Graphics2D g2d=(Graphics2D)g.create();
        Stroke s=new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(s);
         for(Restriccion r:restriccion){
            ArrayList<PuntoSolucion>puntos = r.intersecciónDeLosPuntos();
       
            if(puntos .size()>0){ 
                if(puntos .size()==1){
                    if(r.x2==0){
                       PuntoSolucion p1=new PuntoSolucion(r.signoCoeficiente/r.x1,maxY); 
                       PuntoSolucion p2=new PuntoSolucion(r.signoCoeficiente/r.x1,-maxY); 
                       p1=this.puntoNegativo(p1, maxX, maxY);
                       p2=this.puntoNegativo(p2, maxX, maxY);
                     
                        g2d.setColor(Color.black);
                        if(r.tipoF.equals(Restriccion.igual)){
                           g2d.setColor(Color.red);
                        }
                        g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y); 
                    }
                    else{
                        try{
                            PuntoSolucion p1=new PuntoSolucion(-maxX,r.valor0Y(-maxX)); 
                            PuntoSolucion p2 =new PuntoSolucion(maxX,r.valor0Y(maxX)); 
                            
                            p1=this.puntoNegativo(p2, maxX, maxY);
                            p2=this.puntoNegativo(p2, maxX, maxY);
                            g2d.setColor(Color.black);
                             if(r.tipoF.equals(Restriccion.igual)){
                                g2d.setColor(Color.red); 
                            }
                            g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                        }catch(Exception e){   
                        }  
                    } 
                }
                else{
                    try{
                        PuntoSolucion p1 = new PuntoSolucion(-maxX,r.valor0Y(-maxX)); 
                        PuntoSolucion p2=new PuntoSolucion(maxX,r.valor0Y(maxX)); 
                        p1 = this.puntoNegativo(p1, maxX, maxY);
                        p2 = this.puntoNegativo(p2, maxX, maxY);

                        g2d.setColor(Color.black);
                        if(r.tipoF.equals(Restriccion.igual)){
                            g2d.setColor(Color.red);
                        }
                        g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                    }catch(Exception e){
                    }   
                }
            }
        }
    }
    public void tRestriccionNoNegativo(Graphics g,double maxX, double maxY){
        Graphics2D g2d=(Graphics2D)g.create();
        ArrayList<Restriccion>restriccion = cs.restriccion;
        Stroke s=new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(s);
        int cont=0;
        for(Restriccion r:restriccion ){
            if(r.tipoF.equals(Restriccion.menorOigual) || r.tipoF.equals(Restriccion.mayorOigual) ){
                cont++;
            }
        }
        if(cont==0){
            cont=1;
        }
        Color drawColor=new Color(0,0,0,0/cont);
        
        for(Restriccion r:restriccion ){
            ArrayList<PuntoSolucion>puntos=r.intersecciónDeLosPuntos();
            g2d.setColor(drawColor);
            if(puntos.size()>0){
                 
                if(puntos.size()==1){
                    if(r.x2==0){
                       PuntoSolucion p1 = new PuntoSolucion(r.signoCoeficiente/r.x1,maxY); 
                       PuntoSolucion p2 = new PuntoSolucion(r.signoCoeficiente/r.x1,-maxY);
                       
                       p1=this.puntoNoNegativo(p1, maxX, maxY);
                       p2=this.puntoNoNegativo(p2, maxX, maxY);
                       
                       if(r.tipoF.equals(Restriccion.menorOigual)){
                            int[]xs={10,(int)p1.x,(int)p2.x,10};
                            int[]ys={10,10,this.getHeight()-10,this.getHeight()-10};
                            g2d.fillPolygon(xs, ys, 4);
                        }
                        else if(r.tipoF.equals(Restriccion.mayorOigual)){
                            int[]xs={(int)p1.x,this.getWidth()-10,this.getWidth()-10,(int)p2.x};
                            int[]ys={10,10,this.getHeight()-10,this.getHeight()-10};
                            g2d.fillPolygon(xs, ys,4);
                        }
                        g2d.setColor(Color.black);
                        g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);   
                    }
                    else{
                        try{
                            PuntoSolucion p1=new PuntoSolucion(-maxX,r.valor0Y(-maxX)); 
                            PuntoSolucion p2=new PuntoSolucion(maxX,r.valor0Y(maxX)); 
                            
                            p1=this.puntoNoNegativo(p1, maxX, maxY);
                            p2=this.puntoNoNegativo(p2, maxX, maxY);
                            
                            if(r.tipoF.equals(Restriccion.menorOigual)){
                                if(r.x2>0){
                                    int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                    int[]ys={(int)p1.y,(int)p2.y,this.getHeight()-10,this.getHeight()-10};
                                    g2d.fillPolygon(xs, ys,4);
                                }else{
                                    int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                    int[]ys={(int)p1.y,(int)p2.y,10,10};
                                    g2d.fillPolygon(xs, ys,4); 
                                }  
                            }else if(r.tipoF.equals(Restriccion.mayorOigual)){
                                if(r.x2>0){
                                    int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                    int[]ys={(int)p1.y,(int)p2.y,10,10};
                                    g2d.fillPolygon(xs, ys,4);
                                }else{
                                    int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                    int[]ys={(int)p1.y,(int)p2.y,this.getHeight()-10,this.getHeight()-10};
                                    g2d.fillPolygon(xs, ys,4);
                                }
                            }
                            g2d.setColor(Color.black);
                            g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                        }catch(Exception e){}
                    }  
                }else{
                     try{
                        PuntoSolucion p1=new PuntoSolucion(-maxX,r.valor0Y(-maxX)); 
                        PuntoSolucion p2 = new PuntoSolucion(maxX,r.valor0Y(maxX)); 
 
                        p1=this.puntoNoNegativo(p1, maxX, maxY);
                        p2=this.puntoNoNegativo(p2, maxX, maxY);
                        if(r.tipoF.equals(Restriccion.menorOigual)){
                            if(r.x2>0){
                            int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                            int[]ys={(int)p1.y,(int)p2.y,this.getHeight()-10,this.getHeight()-10};
                            g2d.fillPolygon(xs, ys,4);  
                              }else{
                                    int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                               int[]ys={(int)p1.y,(int)p2.y,10,10};
                               g2d.fillPolygon(xs, ys,4);
                              } 
                        }else if(r.tipoF.equals(Restriccion.mayorOigual)){
                            if(r.x2>0){
                                int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                int[]ys={(int)p1.y,(int)p2.y,10,10};
                                g2d.fillPolygon(xs, ys,4); 
                            }
                            else{
                                int[]xs={(int)p1.x, (int)p2.x,this.getWidth()-10,10};
                                int[]ys={(int)p1.y,(int)p2.y,this.getHeight()-10,this.getHeight()-10};
                                g2d.fillPolygon(xs, ys,4); 
                            }   
                        }
                        g2d.setColor(Color.black);
                        g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                    }catch(Exception e){}   
                }
            }
        }   
    }
    
   @Override
    public void paint(Graphics g){
        if(cs==null){
            int w=this.getWidth();
            int h=this.getHeight();
            g.setColor(Color.white);
            g.fillRect(0, 0, w, h);
        }else{
            if(cs.valorNegativo){
            int w=this.getWidth();
            int h=this.getHeight();
             g.setColor(Color.white);
            g.fillRect(0, 0, w, h);
          
            ArrayList<PuntoSolucion> puntos=cs.posiblesPuntos();
            if(puntos.size()>0){
                 double maxX=Double.NEGATIVE_INFINITY;
                for (int i = 0; i < puntos.size(); i++) {
                    if(Math.abs(puntos.get(i).x)>maxX){
                        maxX=Math.abs(puntos.get(i).x);
                    }
                }
                double maxY=Double.NEGATIVE_INFINITY;
                for (int i = 0; i < puntos.size(); i++) {
                    if(Math.abs(puntos.get(i).y)>maxY){
                        maxY=Math.abs(puntos.get(i).y);
                    }
                }
                if(maxX==0){
                    maxX=10;
                }
                
                if(maxY==0){
                    maxY=10;
                }
                
              
                this.restriccionNegativa(g, maxX, maxY);
                this.lineaNegativa(g, maxX, maxY);
                this.trazarPlano(g);
                this.ejesConNegativos(g);
                this.numerosNegativos(g, maxX, maxY);
                this.TPuntoPosible(g, maxX, maxY);
            } 
        }else{
            int w=this.getWidth();
            int h=this.getHeight();
            g.setColor(Color.white);
            g.fillRect(0, 0, w, h);
            
             ArrayList<PuntoSolucion> puntos=cs.posiblesPuntos();
             if(puntos.size()>0){
                 for (int i = 0; i < puntos.size(); i++) {
                     if(puntos.get(i).x<0 || puntos.get(i).y<0 ){
                         puntos.remove(i);
                         i--;
                     }
                     
                 }
                 
                  double maxX=Double.NEGATIVE_INFINITY;
                for (int i = 0; i < puntos.size(); i++) {
                    if((puntos.get(i).x)>maxX){
                        maxX=(puntos.get(i).x);
                    }
                }
                
                double maxY=Double.NEGATIVE_INFINITY;
                for (int i = 0; i < puntos.size(); i++) {
                    if((puntos.get(i).y)>maxY){
                        maxY=(puntos.get(i).y);
                    }
                }
                if(maxX==0){
                    maxX=10;
                }
                
                if(maxY==0){
                    maxY=10;
                }
                 
               
                 this.tRestriccionNoNegativo(g, maxX, maxY);
                 this.planoNoNegativo(g, maxX, maxY);
                 this.trazarPlano(g);
                 this.ejesSinNegativos(g);
                  this.numerosNoNegativos(g, maxX, maxY);
                 this.TPuntoPosible(g, maxX, maxY);
             }
        }
        }
        
    }
   
   
    
    
    
    
}
