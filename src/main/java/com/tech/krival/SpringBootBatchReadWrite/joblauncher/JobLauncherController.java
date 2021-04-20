package com.tech.krival.SpringBootBatchReadWrite.joblauncher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

public class JobLauncherController {
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	Job job;
	
	@RequestMapping("/launchJob")
	public String handle() throws Exception {
		 JobParameters params = new JobParametersBuilder()
                 .addLong("time", System.currentTimeMillis())
                 .toJobParameters();
     jobLauncher.run(job, params);
		return "Done";
		
	}
	

}
