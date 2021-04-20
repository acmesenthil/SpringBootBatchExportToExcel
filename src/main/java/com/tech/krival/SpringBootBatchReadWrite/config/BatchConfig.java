package com.tech.krival.SpringBootBatchReadWrite.config;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.batch.core.JobExecutionListener;

import com.tech.krival.SpringBootBatchReadWrite.listener.JobListener;
import com.tech.krival.SpringBootBatchReadWrite.model.User;
import com.tech.krival.SpringBootBatchReadWrite.writer.BookWriter;

@Configuration
@EnableBatchProcessing

public class BatchConfig {
	
	private static final String EXPORT_FILENAME = "export.xlsx";
	
//	@Autowired
//	TaskletStep taskletStep;
	
	@Autowired
	  public JobBuilderFactory jobBuilderFactory;

	  @Autowired
	  public StepBuilderFactory stepBuilderFactory;
	  

	  
	  @Bean
	    public Workbook workbook() {
	        return new SXSSFWorkbook(100);
	    }

	    @Bean
	    public FileOutputStream fileOutputStream() throws FileNotFoundException {
	        return new FileOutputStream(EXPORT_FILENAME);
	    }
	  
	  @Bean
	  public StaxEventItemReader<User> bookReader(){
	   StaxEventItemReader<User> reader = new StaxEventItemReader<User>();
	   try {
	   reader.setResource(new ClassPathResource("user.xml"));
	   reader.setFragmentRootElementName("user");
	   
	   Map<String, String> aliases = new HashMap<String, String>();
	   aliases.put("user", "com.tech.krival.SpringBootBatchReadWrite.model.User");
	   
	   XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
	 
		xStreamMarshaller.setAliases(aliases);
	
	   
	   reader.setUnmarshaller(xStreamMarshaller);
	   
	   } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	   return reader;
	  }
	  
	  @Bean
	    public ItemWriter<User> bookWriter(SXSSFWorkbook workbook) {
	        SXSSFSheet sheet = workbook.createSheet("Users");
	        return new BookWriter(sheet);
	    }
	  
	  

	  @Bean
	  public Step step1(ItemReader<User> reader, ItemWriter<User> writer) {
	   return stepBuilderFactory.get("step1")
	     .<User, User> chunk(10)
	     .reader(reader)
	     .writer(writer)
	     .build();
	  }
	  

	  
	  
	  @Bean
	    public Job job(Step step, JobExecutionListener listener) {
	        return jobBuilderFactory.get("exportBooksToXlsx")
	                .start(step)
	                .listener(listener)
	                .build();
	    }
	  
	  

	  
	  
	  

	    @Bean
	    JobListener jobListener(SXSSFWorkbook workbook, FileOutputStream fileOutputStream) throws IOException {
	        return new JobListener(workbook, fileOutputStream);
	    }

}
