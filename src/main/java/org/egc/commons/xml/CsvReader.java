package org.egc.commons.xml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lp on 2017/5/3.
 */
public class CsvReader {

    public List<String> csvHeaderRead(String csvPath){
        BufferedReader buffer;
        List<String> filds = new ArrayList<>();
        try{
            buffer = new BufferedReader(new FileReader(csvPath));
            String line = buffer.readLine();
            String[] colums = line.split(",");
            for(String colum : colums){
                filds.add(colum);
            }
            buffer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return filds;
    }

//TODO:添加子都的模糊匹配
    public String getGeneralName(){
        return "";
    }

    //读取样点，第一行表头也包括在内
    public List<String> readSamples(String csvPath){
        BufferedReader bufferedReader;
        List<String> samples = new ArrayList<>();
        try{
            bufferedReader = new BufferedReader(new FileReader(csvPath));
            String line ;
            while((line = bufferedReader.readLine()) != null){
                samples.add(line);
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return samples;
    }
}
