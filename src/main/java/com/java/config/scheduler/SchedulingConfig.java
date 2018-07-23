package com.java.config.scheduler;

import com.java.sys.common.utils.Tool;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.io.PrintWriter;
import java.io.StringWriter;


@Configuration
public class SchedulingConfig implements SchedulingConfigurer {
    private final ThreadPoolTaskScheduler taskScheduler;

    SchedulingConfig() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("sys-scheduled-");
        taskScheduler.initialize();
        taskScheduler.setErrorHandler(throwable ->{
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw,true));
            Tool.error(sw.toString(), this.getClass());
        });
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler);
    }
}
