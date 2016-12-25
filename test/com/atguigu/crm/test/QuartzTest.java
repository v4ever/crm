package com.atguigu.crm.test;

import java.text.ParseException;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * 在 Spring 中使用 Quartz:
 * 1. 加入 Quartz 的 2 个 jar 包: 
 * quartz-2.2.1.jar
 * quartz-jobs-2.2.1.jar
 * 
 * 2. 加入 Spring 的一个 jar 包:
 * spring-context-support-4.0.0.RELEASE.jar
 * 
 * 3. 配置 Spring 的配置文件: 
 * 1). 配置需要执行的任务的 bean. 但该 bean 不需要再实现任何接口
 * 2). 在 Spring 的配置文件中配置 JobDetail: 具体使用 MethodInvokingJobDetailFactoryBean.
 * ①.关联需要执行任务的 bean.
 * ②.需要执行 bean 中的方法.  
 * 3). 配置 CronTrigger: 具体使用 CronTriggerFactoryBean
 * ①.设定 Cron 表达式
 * ②.关联 JobDetail
 * 4). 配置 Scheduler: 具体使用  SchedulerFactoryBean
 * ①.制定关联的 Trigger 的数组. 
 * ②.可以设定 Quartz 的基本属性. Quartz 的基本属性配置在 /org/quartz/quartz.properties 中.
 * ③.指定 IOC 容器启动多久后才开始执行 Quartz
 */
public class QuartzTest {

	public static void main(String[] args) throws SchedulerException, ParseException{
//		实现 Job 接口，可使 Java 类变为可调度的任务
//		创建描述 Job 的 JobDetail 对象
		JobDetailImpl jobDetail = new JobDetailImpl();
		jobDetail.setName("myJobDetail");
		jobDetail.setGroup("myJobGroup");
		jobDetail.setJobClass(MyJob.class);
		
		/*
//		创建 SimpleTrigger 对象
		SimpleTriggerImpl simpleTrigger = new SimpleTriggerImpl();
		simpleTrigger.setName("mySimpleTigger");
		simpleTrigger.setGroup("mySimpleTriggerGroup");
		
//		设置触发 Job 执行的时间规则
		simpleTrigger.setStartTime(new Date());
		simpleTrigger.setRepeatInterval(1000 * 5);
		simpleTrigger.setRepeatCount(10);
		*/
		
		CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setName("myCronTrigger");
		trigger.setGroup("myCronTrigerGroup");
		//设定 Cron 表达式
		trigger.setCronExpression("0/3 8 9 30 1 ? 2016");
		
//		通过 SchedulerFactory 获取 Scheduler 对象
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		
//		向 SchedulerFactory 中注册 JobDetail 和 Trigger
		scheduler.scheduleJob(jobDetail, trigger);
		
//		启动调度任务
		scheduler.start();
	}

}
