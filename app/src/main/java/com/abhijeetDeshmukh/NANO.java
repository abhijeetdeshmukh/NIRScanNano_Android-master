package com.abhijeetDeshmukh;

import java.util.ArrayList;

/*** Created by ABHIJEET on 01-09-2017. */

public class NANO {

    private ArrayList<Double> mOrganicData = new ArrayList<Double>();
    private ArrayList<Double> mSampleData = new ArrayList<Double>();

    private double mKLDpq = 0 ;
    private double mKLDqp = 0 ;
    // Spectral angle distance
    private double mSAD = 0 ;
    //Spectral Information Divergence
    private double mSID = 0 ;

    //constructor
    public  NANO( ArrayList<Double> x, ArrayList<Double> y){
        mOrganicData = x ;
        mSampleData = y ;
    }

    public void processNANO(){
        calculateKLDpq();
        calculateKLDqp();
        calculateSAD () ;
    }

    public double getKLDpq() {
        return mKLDpq ;
    }
    private void calculateKLDpq(){
        for (int i = 0 ; i< mOrganicData.size() ; i++){
            mKLDpq += mOrganicData.get(i) * Math.log( mOrganicData.get(i)/mSampleData.get(i));
        }
    }

    public double getKLDqp() {
        return mKLDqp ;
    }
    private void calculateKLDqp(){
        for (int i = 0 ; i< mSampleData.size() ; i++){
            mKLDqp += mSampleData.get(i) * Math.log( mSampleData.get(i) / mOrganicData.get(i));
        }
    }

    public double getSAD(){
        return mSAD ;
    }

    private void calculateSAD () {
        mSAD = Math.acos( getInnerProduct(mOrganicData,mSampleData)
                / (getNorm(mOrganicData)* getNorm(mSampleData)) )  // in degrees
                * (180/Math.PI); // in radians
    }
    private double getInnerProduct (ArrayList<Double> x, ArrayList<Double> y){
        double innerProduct = 0 ;
        for (int i = 0; i < x.size(); i++){
            innerProduct += x.get(i) * y.get(i) ;
        }
        return innerProduct ;
    }
    private double getNorm (ArrayList<Double> x) {
        double norm = 0;
        for (int i=0; i< x.size() ; i++){
            norm += x.get(i) * x.get(i) ;
        }
        return Math.sqrt( norm ) ;
    }

    public double getSID () {
        return mKLDpq + mKLDqp ;
    }

}
