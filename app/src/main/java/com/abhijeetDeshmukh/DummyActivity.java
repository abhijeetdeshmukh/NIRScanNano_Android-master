package com.abhijeetDeshmukh;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.kstechnologies.NanoScan.GraphActivity;
import com.kstechnologies.NanoScan.R;
import com.kstechnologies.nirscannanolibrary.SettingsManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.lang.Float.parseFloat;

public class DummyActivity extends AppCompatActivity {

    private Context mContext;

    private String file1Name;
    private String file2Name;

    private ArrayList<String> mXValues;

    private ArrayList<Entry> mAbsorbanceFloat;
    private ArrayList<Entry> mIntensityFloat;
    private ArrayList<Entry> mReflectanceFloat;

    private ArrayList<Double> mProfile1List;
    private ArrayList<Double> mProfile2List;

    ArrayList<Double> moReflectanceDouble = new ArrayList<>();
    ArrayList<Double> moIntensityDouble = new ArrayList<>();
    ArrayList<Double> moAbsorbanceDouble = new ArrayList<>();
    ArrayList<Double> moWavelengthDouble = new ArrayList<>();

    ArrayList<Double> mReflectanceDouble = new ArrayList<>();
    ArrayList<Double> mIntensityDouble = new ArrayList<>();
    ArrayList<Double> mAbsorbanceDouble = new ArrayList<>();
    ArrayList<Double> mWavelengthDouble = new ArrayList<>();

    private TextView mFile1NameTV, mFile2NameTV;

    private TextView mKLDivergancePQValueTV,mKLDiverganceQPValueTV, mSADValueTV, mSIDValueTV, mAbsIntTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        mContext = this;

    }

    @Override
    public void onResume(){
        super.onResume();

        mFile1NameTV = (TextView) findViewById(R.id.tv_file1_name);
        mFile2NameTV = (TextView) findViewById(R.id.tv_file2_name);
        mKLDivergancePQValueTV = (TextView) findViewById(R.id.tv_kld_value);
        mKLDiverganceQPValueTV = (TextView) findViewById(R.id.tv_kld_qp_value);
        mSADValueTV = (TextView) findViewById(R.id.tv_sad_value);
        mSIDValueTV = (TextView) findViewById(R.id.tv_sid_value);
        mAbsIntTV = (TextView) findViewById(R.id.tv_absorbance_and_intensity);

        mProfile1List = new ArrayList<Double>();
        mProfile2List = new ArrayList<Double>();

        file1Name = "marketSamp1300817125454.csv";
        file2Name = "OrganicSamp1300817125552.csv";

        mFile1NameTV.setText(file1Name);
        getFilesData(file1Name, 1);  //1 ;; market sample
        Toast.makeText(DummyActivity.this, "got first data", Toast.LENGTH_SHORT).show();

        mFile2NameTV.setText(file2Name);
        getFilesData(file2Name, 2);  //2 ;; organic sample
        Toast.makeText(DummyActivity.this, "got second data", Toast.LENGTH_SHORT).show();

        NANO nanoReflectance = new NANO(mReflectanceDouble, moReflectanceDouble);
        Toast.makeText(DummyActivity.this, "nano : started", Toast.LENGTH_SHORT).show();
        nanoReflectance.processNANO();
        Toast.makeText(DummyActivity.this, "nano : completed", Toast.LENGTH_SHORT).show();
        mKLDivergancePQValueTV.setText(String.valueOf(nanoReflectance.getKLDpq()));
        mKLDiverganceQPValueTV.setText(String.valueOf(nanoReflectance.getKLDqp()));
        mSADValueTV.setText(String.valueOf(nanoReflectance.getSAD()));
        mSIDValueTV.setText(String.valueOf(nanoReflectance.getSID()));


        NANO nanoAbsorbance = new NANO(mAbsorbanceDouble, moAbsorbanceDouble);
        nanoAbsorbance.processNANO();
        String KLDivergancePQValue = String.valueOf(nanoAbsorbance.getKLDpq());
        String KLDiverganceQPValue = String.valueOf(nanoAbsorbance.getKLDqp()) ;
        String SADValue = String.valueOf(nanoAbsorbance.getSAD());
        String SIDValue = String.valueOf(nanoAbsorbance.getSID());
        String analysis = "Absorbance : \n"  + // "File selected : " + fileSelectedName + "\n" +
                "KL diverge PQ : " + KLDivergancePQValue + "\n" +
                "KL diverge QP : " + KLDiverganceQPValue + "\n" +
                " SAD : " + SADValue + "\n" +
                " SID : " + SIDValue + "\n";

        NANO nanoIntensity = new NANO(mIntensityDouble, moIntensityDouble);
        nanoAbsorbance.processNANO();
         KLDivergancePQValue = String.valueOf(nanoAbsorbance.getKLDpq());
         KLDiverganceQPValue = String.valueOf(nanoAbsorbance.getKLDqp()) ;
         SADValue = String.valueOf(nanoAbsorbance.getSAD());
         SIDValue = String.valueOf(nanoAbsorbance.getSID());
         analysis = analysis + "Intensity : \n"  + // "File selected : " + fileSelectedName + "\n" +
                "KL diverge PQ : " + KLDivergancePQValue + "\n" +
                "KL diverge QP : " + KLDiverganceQPValue + "\n" +
                " SAD : " + SADValue + "\n" +
                " SID : " + SIDValue + "\n";

        mAbsIntTV.setText( analysis );

    }

    private void getFilesData(String fileName, int type) {

        mXValues = new ArrayList<>();

        ArrayList<String> mIntensityString = new ArrayList<>();
        ArrayList<String> mAbsorbanceString = new ArrayList<>();
        ArrayList<String> mReflectanceString = new ArrayList<>();

        BufferedReader reader = null;
        BufferedReader dictReader = null;
        InputStream is = null;

        /*^
         * Try to open the file. First from the raw resources, then from the external directory
         * if that fails
         */
        try {
            is = getResources().openRawResource(getResources().getIdentifier(fileName, "raw", getPackageName()));
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Resources.NotFoundException e) {
            try {
                reader = new BufferedReader(new FileReader(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName));
                dictReader = new BufferedReader(new FileReader(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName.replace(".csv", ".dict")));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                Toast.makeText(mContext, getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        //Read lines in from the file
        try {
            String line;
            if (reader != null) {
                while ((line = reader.readLine()) != null) {
                    String[] RowData = line.split(",");
                    if (RowData[0].equals("(null)")) {
                        mXValues.add("0");
                    } else {
                        if (RowData[0].equals("Wavelength")) {
                            mXValues.add(RowData[0]);
                        } else {
                            mXValues.add(getSpatialFreq(RowData[0]));
                        }
                    }
                    if (RowData[1].equals("(null)")) {
                        mIntensityString.add("0");
                    } else {
                        mIntensityString.add(RowData[1]);
                    }
                    if (RowData[2].equals("(null)")) {
                        mAbsorbanceString.add("0");
                    } else {
                        mAbsorbanceString.add(RowData[2]);
                    }
                    if (RowData[3].equals("(null)")) {
                        mReflectanceString.add("0");
                    } else {
                        mReflectanceString.add(RowData[3]);
                    }
                }
            }
        } catch (IOException ex) {
            // handle exception
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // handle exception
            }
        }

        //Remove the first items since these are column labels
        mXValues.remove(0);
        mIntensityString.remove(0);
        mAbsorbanceString.remove(0);
        mReflectanceString.remove(0);

        //Generate data points and calculate mins and maxes
        if (type == 1){
            for (int i = 0; i < mXValues.size(); i++) {
                try {
                    Double fAbsorbance = Double.parseDouble(mAbsorbanceString.get(i));
                    Double fIntensity = Double.parseDouble(mIntensityString.get(i));
                    Double fReflectance = Double.parseDouble(mReflectanceString.get(i));
                    Double fWavelength = Double.parseDouble(mXValues.get(i));

                    mAbsorbanceDouble.add(fAbsorbance);
                    mIntensityDouble.add(fIntensity);
                    mReflectanceDouble.add(fReflectance);
                    mWavelengthDouble.add(fWavelength);

                } catch (NumberFormatException e) {
                    Toast.makeText(DummyActivity.this, "Error parsing float value", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } else {
            for (int i = 0; i < mXValues.size(); i++) {
                try {
                    Double fAbsorbance = Double.parseDouble(mAbsorbanceString.get(i));
                    Double fIntensity = Double.parseDouble(mIntensityString.get(i));
                    Double fReflectance = Double.parseDouble(mReflectanceString.get(i));
                    Double fWavelength = Double.parseDouble(mXValues.get(i));

                    moAbsorbanceDouble.add(fAbsorbance);
                    moIntensityDouble.add(fIntensity);
                    moReflectanceDouble.add(fReflectance);
                    moWavelengthDouble.add(fWavelength);

                } catch (NumberFormatException e) {
                    Toast.makeText(DummyActivity.this, "Error parsing float value", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    /** Function return the specified frequency in units of frequency or wavenumber
     *
     * @param freq The frequency to convert
     * @return string representing either frequency or wavenumber
     */
    private String getSpatialFreq(String freq) {
        Float floatFreq = parseFloat(freq);
        if (SettingsManager.getBooleanPref(mContext, SettingsManager.SharedPreferencesKeys.spatialFreq, SettingsManager.WAVELENGTH)) {
            return String.format("%.02f", floatFreq);
        } else {
            return String.format("%.02f", (10000000 / floatFreq));
        }
    }
}