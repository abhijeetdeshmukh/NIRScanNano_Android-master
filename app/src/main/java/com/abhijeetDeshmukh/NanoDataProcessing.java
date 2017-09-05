package com.abhijeetDeshmukh;

/*** Created by ABHIJEET on 01-09-2017. */

public class NanoDataProcessing {

    private double[] mOrganicData = new double[227];
    private double[] mSampleData = new double[227];

    private double mKLDpq = 0 ;
    private double mKLDqp = 0 ;
    // Spectral angle distance
    private double mSAD = 0 ;
    //Spectral Information Divergence
    private double mSID = 0 ;

    //constructor
    public NanoDataProcessing(double [] x, double [] y){
        mOrganicData = x ;
        mSampleData = y ;

        calculateKLDpq();
        calculateKLDqp();
        calculateSAD();
    }

    public double getKLDpq() {
        return mKLDpq ;
    }
    private void calculateKLDpq(){
        for (int i = 0 ; i< mOrganicData.length ; i++){
            mKLDpq += mOrganicData[i] * Math.log( mOrganicData[i]/mSampleData[i]);
        }
    }

    public double getKLDqp() {
        return mKLDqp ;
    }
    private void calculateKLDqp(){
        for (int i = 0 ; i< mSampleData.length ; i++){
            mKLDqp += mSampleData[i] * Math.log( mSampleData[i]/mOrganicData[i]);
        }
    }

    // in radians
    public double getSAD () {
        return mSAD ;
    }
    private void calculateSAD () {
        mSAD = Math.acos( getInnerProduct(mOrganicData,mSampleData)
                / (getNorm(mOrganicData)* getNorm(mSampleData)) )  // in degrees
                * (180/Math.PI); // in radians
    }
    private double getInnerProduct (double [] x, double [] y){
        double innerProduct = 0 ;
        for (int i = 0; i < x.length; i++){
            innerProduct += x[i] * y[i] ;
        }
        return innerProduct ;
    }
    private double getNorm (double[] x) {
        double norm = 0;
        for (int i=0; i< x.length ; i++){
            norm += x[i] * x[i] ;
        }
        return Math.sqrt( norm ) ;
    }

    public double getSID () {
        return mKLDpq + mKLDqp ;
    }

}
