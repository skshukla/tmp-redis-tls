import java.util.*;
import java.util.concurrent.*;


public class SampleTwo {
    final Map<Long, Integer> m = new HashMap<>();

    public static void main(String[] args) throws Exception {
        new SampleTwo().run();
    }

    private void run() throws Exception {
        System.out.println("run-001");


        final ExecutorService manager = Executors.newFixedThreadPool(10);

        for (int i=0; i< 10; i++) {
            manager.submit( new MyTask(m));
        }
        manager.shutdownNow();

        manager.awaitTermination(10, TimeUnit.SECONDS);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(String.format("[%s][STAT] ------------> Map size is : {%d}", Thread.currentThread().getName(), m.size()));
            }
        }, 0, 5000);

    }


//    class MyMapStat implements Runnable {
//
//        private Map<Long, Integer> m = new HashMap<>();
//
//        public MyMapStat(Map<Long, Integer> m) {
//            this.m = m;
//        }
//
//        @Override
//        public void run() {
//
//        }
//    }

    class MyTask implements Callable<Void> {

        private Map<Long, Integer> m = new HashMap<>();

        public MyTask(Map<Long, Integer> m) {
            this.m = m;
        }

        @Override
        public Void call() throws Exception {
            int maxVal = 0;
            while(true) {
                final Date d = new Date();
                final long id = d.getTime();
                final int val = getId(id);
//                System.out.println(String.format("Thread Name = {%s}, id={%d}, counter = {%d}", Thread.currentThread().getName(),
//                        id, val));
                if (val > maxVal) {
                    maxVal = val;
                    System.out.println(String.format("Maximum value for Thread {%s} is {%d} for id {%d}", Thread.currentThread().getName(), maxVal, id));
                }
            }
        }

        private Integer getId(final long l) {
            synchronized (m) {
                final Integer oldVal = m.get(l);
                final Integer newVal = (oldVal == null) ? 1 : oldVal+1;
                m.put(l, newVal);
                return newVal;
            }

        }
    }
}
