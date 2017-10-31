package com.abhijeetDeshmukh;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
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

    private TextView mFile1NameTV, mFile2NameTV;

    private TextView mKLDivergancePQValueTV,mKLDiverganceQPValueTV, mSADValueTV, mSIDValueTV;

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

        mProfile1List = new ArrayList<Double>();
        mProfile2List = new ArrayList<Double>();

        file1Name = "marketSamp1300817125454.csv";
        file2Name = "OrganicSamp1300817125552.csv";

        mFile1NameTV.setText(file1Name);
        mProfile1List = getFilesData(file1Name);
        Toast.makeText(DummyActivity.this, "got first data", Toast.LENGTH_SHORT).show();

        mFile2NameTV.setText(file2Name);
        mProfile2List = getFilesData(file2Name);
        Toast.makeText(DummyActivity.this, "got second data", Toast.LENGTH_SHORT).show();

        NANO nano = new NANO(mProfile1List, mProfile2List);
        Toast.makeText(DummyActivity.this, "nano : started", Toast.LENGTH_SHORT).show();
        nano.processNANO();
        Toast.makeText(DummyActivity.this, "nano : completed", Toast.LENGTH_SHORT).show();
        mKLDivergancePQValueTV.setText(String.valueOf(nano.getKLDpq()));
        mKLDiverganceQPValueTV.setText(String.valueOf(nano.getKLDqp()));
        mSADValueTV.setText(String.valueOf(nano.getSAD()));
        mSIDValueTV.setText(String.valueOf(nano.getSID()));

    }

    private ArrayList<Double> getFilesData(String fileName) {

        mXValues = new ArrayList<>();
        ArrayList<String> mIntensityString = new ArrayList<>();
        ArrayList<String> mAbsorbanceString = new ArrayList<>();
        ArrayList<String> mReflectanceString = new ArrayList<>();

        ArrayList<Double> intensityDouble = new ArrayList<>();
        ArrayList<Double> wavelengthDouble = new ArrayList<>();
        ArrayList<Double> reflectanceDouble = new ArrayList<>();

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
        for (int i = 0; i < mXValues.size(); i++) {
            try {
//                Double fIntensity = Double.parseDouble(mIntensityString.get(i));
//                Float fAbsorbance = Float.parseFloat(mAbsorbanceString.get(i));Float fReflectance = Float.parseFloat(mReflectanceString.get(i));
                Double fWavelength = Double.parseDouble(mXValues.get(i));
                Double fReflectance = Double.parseDouble(mReflectanceString.get(i));
                reflectanceDouble.add(fReflectance);
                wavelengthDouble.add(fWavelength);

            } catch (NumberFormatException e) {
                Toast.makeText(DummyActivity.this, "Error parsing float value", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        return reflectanceDouble ;
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