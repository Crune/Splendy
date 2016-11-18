package org.kh.splendy.task;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Queue;

import org.kh.splendy.service.StreamService;
import org.kh.splendy.vo.SplendyTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.*;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private static Queue<SplendyTask> tasks = new LinkedList<SplendyTask>();

	private static Map<String, Method> schedule = new HashMap<String, Method>();

	// http://kanetami.tistory.com/entry/Schedule-Spring-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8A%A4%EC%BC%80%EC%A5%B4-%EC%84%A4%EC%A0%95%EB%B2%95-CronTab
	@Scheduled(fixedRate = 1000 * 5)
	public void checker() {
		//task();

		//log.info("The time is now {}", dateFormat.format(new Date()));
	}
	/*
	public void task() {
		if (!tasks.isEmpty()) {
			SplendyTask task = tasks.poll();
			try {
				task.getMethod().invoke(task.getObj());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				int repeat = task.getRepeat();
				if (repeat != 0) {
					if (repeat > 0) task.setRepeat(--repeat);
					tasks.add(task);
				}
			}
		}
	}
	
	public void addTask(SplendyTask task) {
		tasks.add(task);
	}
	 */
}
