package main;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneralThreadPool {

	public static GeneralThreadPool get() {
		return Inner.instance;
	}

	private static class Inner {
		private static final GeneralThreadPool instance = new GeneralThreadPool();
	}

	// 預設 ScheduledThreadPool 大小
	private static final int SCHEDULED_CORE_POOL_SIZE = 10;

	private final Executor executor;

	private final ScheduledExecutorService scheduler;

	private GeneralThreadPool() {

		executor = Executors.newCachedThreadPool();

		scheduler = Executors.newScheduledThreadPool(						//
				SCHEDULED_CORE_POOL_SIZE,									//
				new PriorityThreadFactory("GSTPool", Thread.NORM_PRIORITY) 	//
		);

	}

	/**
	 * 執行 Runnable
	 */
	public void execute(final Runnable r) {
		try {
			if (executor == null) {
				final Thread t = new Thread(r);
				t.start();

			} else {
				executor.execute(r);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 執行 Thread
	 */
	public void execute(final Thread t) {
		try {
			t.start();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 延時執行 Runnable
	 */
	public ScheduledFuture<?> schedule(final Runnable r, final long delay) {
		try {
			if (delay <= 0) {
				executor.execute(r);
				return null;
			}
			return scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);

		} catch (final RejectedExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 固定時間間隔執行 Runnable
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(final Runnable r, final long initialDelay, final long period) {
		try {
			return this.scheduler.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.MILLISECONDS);

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取消
	 */
	public void cancel(final ScheduledFuture<?> future, final boolean mayInterruptIfRunning) {
		try {
			future.cancel(mayInterruptIfRunning);

		} catch (final RejectedExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 固定時間間隔執行 TimerTask
	 */
	public Timer scheduleAtFixedRate(final TimerTask task, final long delay, final long period) {
		try {
			final Timer timer = new Timer();
			timer.scheduleAtFixedRate(task, delay, period);
			return timer;

		} catch (final RejectedExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取消
	 */
	public void cancel(final TimerTask task) {
		try {

			task.cancel();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 實作 ThreadFactory 主要是為了控制在創建執行緒時的行為，
	 例如指定執行緒名稱、優先級、守護線程等。
	 使用 ThreadFactory 可以讓程式碼更具有可讀性和可維護性，
	 並且可以更加靈活地管理執行緒。
	
	 例如，
	 在執行緒池中使用 ThreadFactory 可以將一些通用的行為，
	 例如指定執行緒名稱和優先級，進行統一管理，
	 這樣可以更好地控制程式碼的可讀性和可維護性，
	 同時也可以更有效地管理執行緒。
	 * */
	private class PriorityThreadFactory implements ThreadFactory {

		private final int priority;

		private final String name;

		private final AtomicInteger threadNumber = new AtomicInteger(1);

		private final ThreadGroup group;

		public PriorityThreadFactory(final String name, final int prio) {
			this.priority = prio;
			this.name = name;
			this.group = new ThreadGroup(this.name);
		}

		@Override
		public Thread newThread(final Runnable r) {
			final Thread t = new Thread(this.group, r);
			t.setName(this.name + "-" + this.threadNumber.getAndIncrement());
			t.setPriority(this.priority);
			return t;
		}
	}
}
