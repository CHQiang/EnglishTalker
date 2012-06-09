package com.leisuretime.net;

/**
 * @author oDon
 * @date 10/11/1
 * @desc wrap ThreadPoolExecutor & ArrayBlockingQueue to handle parallel thread working
 * ArrayBlockingQueue perform better than LinkedBlockingQueue, but poolsize is limited.
 * */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolWrap {

	private static ThreadPoolWrap instance = null;

	private BlockingQueue<Runnable> bq;

	private ThreadPoolExecutor executor = null;

	private ThreadPoolWrap() {

		bq = new ArrayBlockingQueue<Runnable>(DEFAULT_QUEUESIZE);

		executor = new ThreadPoolExecutor(DEFAULT_MIN_POOLSIZE,
				DEFAULT_MAX_POOLSIZE, DEFAULT_KEEP_ALIVE_TIME,
				TimeUnit.SECONDS, bq, new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static ThreadPoolWrap getThreadPool() {
		if (instance == null)
			instance = new ThreadPoolWrap();

		return instance;
	}

	public void executeTask(Runnable task) {
		executor.execute(task);
	}

	public void shutdown() {
		executor.shutdown();
		instance = null;
	}

	private static int DEFAULT_QUEUESIZE = 5;
	private static int DEFAULT_MIN_POOLSIZE = 3;
	private static int DEFAULT_MAX_POOLSIZE = 7;
	private static long DEFAULT_KEEP_ALIVE_TIME = 3;
}