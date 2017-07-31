package com.omadi.services;

import com.omadi.entities.Base;
import com.omadi.entities.Output;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CSVWriterService {
    private CSVWriter writer;

    public void start(String fileName, Class clazz) throws IOException {
        String currentTime = "_" + LocalDateTime.now();
        currentTime = currentTime.replace(":", "-");
        currentTime = currentTime.replace(".", "-");
        fileName = "csv_files" + File.separator + String.format("%s_%s.csv", fileName, currentTime);

        writer = new CSVWriter(new FileWriter(fileName));
        Field[] declaredFields = clazz.getDeclaredFields();
        String[] fields = new String[declaredFields.length];

        for (int i = 0; i < declaredFields.length; i ++) {
            fields[i] = declaredFields[i].getName();
        }

        writer.writeNext(fields);
    }

    public <T extends Base> void save(List<T> list) throws IOException {
        for (T object : list) {
            writer.writeNext(object.toArray());
        }

        close();
    }

    public void close() throws IOException {
        writer.close();
    }
}
