package com.tech.krival.SpringBootBatchReadWrite.listener;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import javax.batch.runtime.BatchStatus;
import static javax.batch.runtime.BatchStatus.COMPLETED;



@Slf4j
public class JobListener implements JobExecutionListener {
	
	private final SXSSFWorkbook workbook;
    private final FileOutputStream fileOutputStream;

    public JobListener(SXSSFWorkbook workbook, FileOutputStream fileOutputStream) {
        this.workbook = workbook;
        
        this.fileOutputStream = fileOutputStream;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus batchStatus = jobExecution.getStatus().getBatchStatus();
        if (batchStatus == COMPLETED) {
            try {
                workbook.write(fileOutputStream);
                fileOutputStream.close();
                workbook.dispose();
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
       
    }

   

}
