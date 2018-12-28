package top.ibase4j.core.util;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UniqueIdUtil {
	private static long uid = 0L;

	private static Lock lock = new ReentrantLock();

	public static String genId() throws Exception {
		lock.lock();
		try {
			long id = 0L;
			do
				id = System.currentTimeMillis();
			while (id == uid);
			uid = id;
			long l1 = id;

			return String.valueOf(l1);
		} finally {
			lock.unlock();
		}

	}

	public static final String getGuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println(UniqueIdUtil.getGuid());
	}

}