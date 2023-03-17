import com.google.common.collect.EvictingQueue;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MYClass {

    public static void main(String[] args) throws Exception {
        new MYClass().run();
    }

    private void run() throws Exception {

        final Queue<Data> queue = EvictingQueue.create(5000);
        final int nTaskAndThreads = 4;
        final ExecutorService manager = Executors.newFixedThreadPool(nTaskAndThreads);
        for(int i=0; i< nTaskAndThreads; i++) {
            manager.submit(new MyRunnable(queue));
        }
        manager.shutdownNow();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                System.out.println(String.format("--------->\n", queue.size()));
                printQueueInfo(queue);
                System.out.println(String.format("\n<----------- Queue size is {%d} ---------\n", queue.size()));
            }
        }, 0, 2000);


        manager.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Done!!");
    }

    private void  printQueueInfo(final Queue<Data> queue) {
        synchronized (queue) {
            Iterator<Data> it = queue.iterator();
            while(it.hasNext()) {
                System.out.println(String.format("[Thread-%s] Data : {%s}", Thread.currentThread().getName(), it.next()));
            }
        }

    }




    @lombok.Data
    @AllArgsConstructor
    class Data {
        private long timestamp;
        private int counter;

        public int incrementAndGet() {
            this.counter = this.counter + 1;
            return this.counter;
        }
    }

    class MyRunnable implements  Runnable {
        Queue<Data> queue;

        public MyRunnable(Queue<Data> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                long ts = new Date().getTime();
                synchronized (this.queue) {
                    int counter = 0;
                    Data d = this.getDataNode(ts);
                    if (d == null) {
                        d = new Data(ts, 1);
                        this.queue.add(d);
                        counter = 1;
                    } else {
                        counter = d.incrementAndGet();
                    }
                }
            }

        }

        private Data getDataNode(long ts) {
            final Iterator<Data> it = this.queue.iterator();
            while (it.hasNext()) {
                Data d = it.next();
                if (d.getTimestamp() == ts) return d;
            }
            return null;
        }
    }
}
