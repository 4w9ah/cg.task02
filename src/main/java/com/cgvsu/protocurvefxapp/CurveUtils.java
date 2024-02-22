package com.cgvsu.protocurvefxapp;

public class CurveUtils {

    public static int fact(int n){
        int result =1;
        for (int i = 1; i<=n; i++) result *= i;
        return result;
    }
    public static double calcCombination(int n, int k) {
        return ((double) fact(n)) /(fact(k)*fact(n-k));
    }
    public static double calcBernstein(int n, int k, double t) {
        return calcCombination(n,k)*Math.pow(t,k)*Math.pow(1-t,n-k);
    }



}

