package io.github.hundanli;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2022/3/18 14:23
 */
public class NamingBenchmark {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamingBenchmark.class);


    @Parameter(names = {"--period", "-p"}, description = "benchmark period, unit is day", required = true)
    private int period = 1;
    @Parameter(names = {"--services", "-s"}, description = "register service count", required = true)
    private int serviceCount = 500;
    @Parameter(names = {"--instances", "-i"}, description = "register instance count per service", required = true)
    private int instancesCount = 5;


    private static ScheduledExecutorService executorService;


    public static void main(String[] args) throws InterruptedException {

        NamingBenchmark benchmark = new NamingBenchmark();
        try {
            JCommander.newBuilder()
                    .programName("java -jar nacos-benchmark.jar")
                    .addObject(benchmark)
                    .build()
                    .parse(args);
        } catch (ParameterException e) {
            e.getJCommander().usage();
            System.exit(1);
        }
        LOGGER.info("Starting benchmark for NamingServer, period: {} days, services: {}, instances: {}",
                benchmark.period, benchmark.serviceCount, benchmark.instancesCount);


        Properties properties = new Properties();
        properties.setProperty("serverAddr", "10.120.11.65:8848,10.120.11.68:8848,10.120.10.71:8848");
        properties.setProperty("namespace", "benchmark");


        List<NamingRegisterTask> taskList = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        executorService = new ScheduledThreadPoolExecutor(Math.max(1 + benchmark.serviceCount >> 2, 300));

        for (int i = 1; i <= benchmark.serviceCount; i++) {
            NamingRegisterTask task = new NamingRegisterTask(i, properties, benchmark.instancesCount);
            executorService.schedule(task, 10 + random.nextInt(10, 20), TimeUnit.SECONDS);
            taskList.add(task);
        }
        LOGGER.info("benchmark started");
        TimeUnit.DAYS.sleep(benchmark.period);
        for (NamingRegisterTask task : taskList) {
            task.stopped = true;
        }
        executorService.shutdown();
    }

    static class NamingRegisterTask implements Runnable {
        private final Integer seq;
        private final int instances;
        private final Properties properties;
        private volatile boolean stopped = false;
        private long lastBeat = System.currentTimeMillis() / 1000;

        public NamingRegisterTask(Integer seq, Properties properties, int instances) {
            this.seq = seq;
            this.properties = properties;
            this.instances = instances;
        }

        @Override
        public void run() {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            try {
                long interval = System.currentTimeMillis() / 1000 - lastBeat;
                NamingService namingService = NamingFactory.createNamingService(properties);
                for (int i = 0; i < instances; i++) {
                    namingService.registerInstance("nacos.benchmark." + seq, "2.2.2.2", 2000 + seq + i);
                }
                LOGGER.info("register instance for nacos.benchmark.{}, interval={} seconds", seq, interval);
                namingService.shutDown();
                int delay = 15 + random.nextInt(30);
                lastBeat = System.currentTimeMillis() / 1000;
                if (!stopped) {
                    executorService.schedule(this, delay, TimeUnit.SECONDS);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
