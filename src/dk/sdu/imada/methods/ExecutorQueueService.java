/*
 *            	Life-Style-Specific-Islands
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public License.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *      
 * This material was developed as part of a research project at 
 * the University of Southern Denmark (SDU - Odense, Denmark) 
 * and the Federal University of Minas Gerais (UFMG - Belo 
 * Horizonte, Brazil). For more information please access:
 * 
 *      	https://lissi.compbio.sdu.dk/ 
 */
package dk.sdu.imada.methods;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * Class creates an Executor Queue Service. It allows 
 * tasks to be independently added and executed one 
 * at time. This service is necessary to not overflow NCBI 
 * services (it could result in being banned). 
 *  
 * @author Eudes Barbosa
 */
public class ExecutorQueueService {
	
	//------  Variable declaration  ------//

	private static final Logger logger = LogManager.getLogger(ExecutorQueueService.class.getName());
	
	/**  Alternative version using LinkedBlockingQueue [unused]. */
	protected static LinkedBlockingQueue<Runnable> tasks = 
			new LinkedBlockingQueue<Runnable>();
		
	/** Pool that executes a single task at a time */
	protected static ThreadPoolExecutor executor;
	
	//protected Consumer consumer;

	
	//------  Declaration end  ------//


	
	/** Creates an Executor that executes a single task at a time. */
	public ExecutorQueueService() {
		executor = new ThreadPoolExecutor(50, 100, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>() );
	}

	/**
	 * Adds new runnable task into class pool. A single 
	 * task will be executed at a time.
	 * 
	 * @param task		Runnable task.
	 * @return 			Returns Future array.
	 */
	public Future<?> addTask(Runnable task) {
		return executor.submit(task);
	}

	/**
	 * Removes a runnable task from class pool.
	 * @param task		Runnable task.
	 */
	public void removeTask(Runnable task) {
		executor.remove(task);
		executor.purge();
	}

	/** Shuts down the executor before leaving the program */
	public static void killExecutor() {
		executor.shutdown();
	}

	/**
	 * Inner-class attempts to use LinkedBlockedQueue 
	 * instead of the ExecutionService. <br>
	 * [unused]
	 */
	protected class Consumer implements Runnable {

		public void run() {
			try {
				if (tasks.size() > 0)				
					(tasks.take()).run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				scheduleNext();
			}        
		}

		/** 
		 * Executes following task in pool. If there is 
		 * no new task, it will sleep.
		 */
		protected synchronized void scheduleNext() {
			if (tasks.size() > 0) {
				run();
			} else {
				try {
					Thread.sleep(1000);
					logger.debug("Should I be awake by now?");
					scheduleNext();
				} catch (InterruptedException ie) {
					//Handle exception
					logger.error("Error while waiting for the next task.");
					ie.printStackTrace();
				}
			}
		}
	}
}
