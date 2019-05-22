/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalDsAssignment;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 *
 * @author Zakkir
 */
public class Circle {
    private double x;
    private double y;
    private int r = (int) (35) ;
    private Color color1;
    
    private boolean reset = false;
    
    Random random = new Random();
    /*---------- Generate circle centre when class is invoked -------------*/
    public Circle(int x,int y){ // This constructor create circle centre at the middle of canvas for first circle.
        this.x = 500+x/2;
        this.y = y/2;
        color1 = new Color(167,219,216);
    }
    
    public Circle() { // Other circles are generated randomly the circle centre until it is not overlapped.
        xD();
        yD();
        color1 = new Color(167,219,216);
    }
    
    /*--------------- Generating Circle centre within range ---------------------*/
    public void xD(){    
        do{
            x = 20 + 500 + r + random.nextInt(NodesPanel.WIDTH);
        }while(x > NodesPanel.WIDTH-r-50);
    }
    
    public void yD(){
        do{
            y = 20 + r + random.nextInt(NodesPanel.HEIGHT);
        }while(y > NodesPanel.HEIGHT-r-50);
        
    }
    // Preventing circle from overlapping each others
    public boolean overlapping(double x,double y,double r){
        double d = Math.sqrt(Math.pow( this.x-x, 2)+ Math.pow(this.y-y, 2));
        double sumOfradius = this.r+r;
        if(d <= sumOfradius )
            return true;
        else
            return false;
    }
    
    public void setReset(boolean b){ reset = b;}
 
    /*------------ Drawing cirlce based on generated centre with fixed radius --------------*/
    public void draw(Graphics2D g){
        g.setColor(color1);
        g.fillOval((int)(x-r),(int)(y-r),2*r,2*r);
        
        g.setStroke(new BasicStroke(5));
        g.setColor(new Color(105,210,231));
        g.drawOval((int)(x-r),(int)(y-r),2*r,2*r);
        g.setColor(new Color(105,210,231).darker());
        g.setStroke(new BasicStroke(2));
        
    }

    
    /* ----------- Send the value to other class for essential uses -----------*/
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getR() {
        return r;
    }
    
    
    
    
    
}
