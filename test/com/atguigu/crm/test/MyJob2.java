package com.atguigu.crm.test;

import java.util.Date;

import org.quartz.JobExecutionException;

public class MyJob2{

	public void doWork() throws JobExecutionException {
		System.out.println("----> do My Job: " + new Date());
	}

}
