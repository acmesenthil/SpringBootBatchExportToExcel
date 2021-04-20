package com.tech.krival.SpringBootBatchReadWrite;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.Collection;

import org.springframework.batch.core.StepExecution;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;

//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.batch.test.JobLauncherTestUtils;
//import org.springframework.batch.test.context.SpringBatchTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tech.krival.SpringBootBatchReadWrite.config.BatchConfig;




//@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SpringBootBatchReadWriteApplication.class, BatchTestConfiguration.class})
public class SpringBootBatchReadWriteApplicationTests {
	
	private static final String TEST_OUTPUT = "export.xlsx";

    private static final String EXPECTED_OUTPUT = "export_expect.xlsx";

    private static final String TEST_INPUT = "test-input.xml";
	
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
	
	 @Autowired
	    private BatchConfig config;
	 
	 
	 
	 private JobParameters defaultJobParameters() {
	        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
	        paramsBuilder.addString("file.input", TEST_INPUT);
	        paramsBuilder.addString("file.output", TEST_OUTPUT);
	        return paramsBuilder.toJobParameters();
	    }


	
	@Test
    public void testEntireJob() throws Exception {
		FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);
        
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
		JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
        
        assertThat(actualJobInstance.getJobName(), is("exportBooksToXlsx"));
        assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
        AssertFile.assertFileEquals(expectedResult, actualResult);
     
	}
	
	
  @Test
  public void givenReferenceOutput_whenStep1Executed_thenSuccess() throws Exception {

      // given
      FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
      FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

      // when
      JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1", defaultJobParameters());
      Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
      ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

      // then
      assertThat(actualStepExecutions.size(), is(1));
      assertThat(actualJobExitStatus.getExitCode(), is("FAILED"));
      AssertFile.assertFileEquals(expectedResult, actualResult);
  }

}
