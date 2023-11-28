package it.unibo.ai.didattica.competition.tablut.util;

import java.util.Random;

public class MyVector {
    public int x,y;
    private static int h1,h2;

    public MyVector(int x, int y){
        this.x = x;
        this.y = y;
    }

    public MyVector(MyVector v){
        this.x = v.x;
        this.y = v.y;
    }

    @Override
    public String toString() {
        return "(" +
                "x=" + x +
                ", y=" + y +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ( !(o instanceof MyVector) ) return false;
        MyVector myVector = (MyVector) o;
        return x == myVector.x && y == myVector.y;
    }//equals

    @Override
    public int hashCode(){
        return (x*h1+y*h1)*h2;
    }//hashCode

    public static void initHash(){
        Random rand = new Random();
        h1 = rand.nextInt(10_000);
        h2 = rand.nextInt(10_000);
    }
}//MyVector
