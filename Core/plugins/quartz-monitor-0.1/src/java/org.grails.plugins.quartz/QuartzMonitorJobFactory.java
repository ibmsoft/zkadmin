package org.grails.plugins.quartz;

import org.codehaus.groovy.grails.plugins.quartz.GrailsJobFactory;
import org.hibernate.SessionFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.spi.TriggerFiredBundle;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Job factory which enhances GrailsJobFactory.
 *
 * @author James Cook
 * @since 0.1
 */
public class QuartzMonitorJobFactory extends GrailsJobFactory {
    static final java.util.Map<String, Map<String, Object>> jobRuns = new HashMap<String, Map<String, Object>>();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        String grailsJobName = bundle.getJobDetail().getName();
        Object job = super.createJobInstance(bundle);
        if (job instanceof GrailsTaskClassJob) {
            Map<String, Object> map;
            if (jobRuns.containsKey(grailsJobName)) {
                map = jobRuns.get(grailsJobName);
            } else {
                map = new HashMap<String, Object>();
                jobRuns.put(grailsJobName, map);
            }
            job = new QuartzDisplayJob((GrailsTaskClassJob) job, map, sessionFactory);
        }
        return job;
    }

    /**
     * Quartz Job implementation that invokes execute() on the GrailsTaskClassJob instance whilst recording the time
     */
    public class QuartzDisplayJob implements Job {
        GrailsTaskClassJob job;
        Map<String, Object> jobDetails;
        private SessionFactory sessionFactory;

        public QuartzDisplayJob(GrailsTaskClassJob job, Map<String, Object> jobDetails, SessionFactory sessionFactory) {
            this.job = job;
            this.jobDetails = jobDetails;
            this.sessionFactory = sessionFactory;
        }

        public void execute(final JobExecutionContext context) throws JobExecutionException {
            jobDetails.clear();
            jobDetails.put("lastRun", new Date());
            jobDetails.put("status", "running");
            long start = System.currentTimeMillis();
            try {
                job.execute(context);
                sessionFactory.getCurrentSession().flush();
            } catch (Throwable e) {
                jobDetails.put("error", e.getMessage());
                jobDetails.put("status", "error");
                if (e instanceof JobExecutionException) {
                    throw (JobExecutionException) e;
                }
                throw new JobExecutionException(e.getMessage(), e);
            }
            jobDetails.put("status", "complete");
            jobDetails.put("duration", System.currentTimeMillis() - start);
        }
    }

}
