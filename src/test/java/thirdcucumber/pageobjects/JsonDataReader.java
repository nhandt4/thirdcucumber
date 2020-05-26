package thirdcucumber.pageobjects;

import com.google.gson.Gson;
import thirdcucumber.common.DriverManager;
import thirdcucumber.stepdefinitions.Sample;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonDataReader {
    DriverManager driverManager = new DriverManager();
    private final String customerFilePath = driverManager.getSamplePath()+ "Sample.json";
    private List<Sample> sampleList;

    public JsonDataReader(){
        sampleList = getSampleData();
    }

    private List<Sample> getSampleData() {
        Gson gson = new Gson();
        BufferedReader bufferReader = null;
        try {
            bufferReader = new BufferedReader(new FileReader(customerFilePath));
            Sample[] samples = gson.fromJson(bufferReader, Sample[].class);
            return Arrays.asList(samples);
        }catch(FileNotFoundException e) {
            throw new RuntimeException("Json file not found at path : " + customerFilePath);
        }finally {
            try { if(bufferReader != null) bufferReader.close();}
            catch (IOException ignore) {}
        }
    }

    public final Sample getSample(String sampleId){
        for(Sample sample : sampleList) {

            if(sample.getSample().equalsIgnoreCase(sampleId)) return sample;
        }
        return null;
    }
}
