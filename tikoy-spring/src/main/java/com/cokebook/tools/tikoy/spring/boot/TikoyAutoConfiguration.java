package com.cokebook.tools.tikoy.spring.boot;

import com.cokebook.tools.tikoy.container.JobContainer;
import com.cokebook.tools.tikoy.container.JobFactory;
import com.cokebook.tools.tikoy.mapping.annotation.JobMapping;
import com.cokebook.tools.tikoy.container.JobSnapshotTrigger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;
import java.util.function.Function;

/**
 * @date 2024/11/12
 */
@Configuration
public class TikoyAutoConfiguration {


    @Bean("job-container")
    public JobContainer jobContainer(ApplicationContext context) {
        return new JobContainerX(
                text -> context.getEnvironment().resolvePlaceholders(text),
                clazz -> context.getBean(clazz)
        );
    }

    @Bean
    public JobSnapshotTrigger snapshotTrigger(JobContainer jobContainer, ApplicationContext applicationContext) {
        return new JobSnapshotTrigger(jobContainer,
                () -> jobContainer.getLogDispatcher(),
                () -> new SpringJdbcOperationsFactory(applicationContext));
    }

    public static class JobContainerX extends JobContainer implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware, DisposableBean {

        private ApplicationContext applicationContext;

        public JobContainerX(Function<String, String> textResolver,
                             Function<Class<? extends JobFactory>, ? extends JobFactory> factoryBuilder) {
            super(textResolver, factoryBuilder);
        }

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            Map<String, Object> mappingBeans = applicationContext.getBeansWithAnnotation(JobMapping.class);
            for (Object object : mappingBeans.values()) {
                this.register(object);
            }
            this.init();
            this.start();
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Override
        public void destroy() throws Exception {
            this.stop();
        }
    }

}
