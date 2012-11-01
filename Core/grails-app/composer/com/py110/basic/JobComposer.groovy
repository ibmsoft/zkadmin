package com.py110.basic

import org.zkoss.zkgrails.GrailsComposer
import org.zkoss.zul.*
import org.quartz.Scheduler
import org.grails.plugins.quartz.TriggerState
import org.grails.plugins.quartz.QuartzMonitorJobFactory
import com.py110.user.Pyuser
import com.py110.pubs.ulit.Ulits

/**
 * 定时任务
 */
class JobComposer extends GrailsComposer {

    Window parent
    Listbox listboxPane
    Pyuser userInfo
    Toolbarbutton tbbfresh,tbbstop,tbbpause,tbbrun/*,tbbsave*//*,tbbuser*/
    /* shcema parameter*/
    def jobsList = []
    static final Map triggers = [:]
    def jobManagerService
    Scheduler quartzScheduler

    String sortTj = "fvkey"
    def afterCompose = { window ->
        userInfo = Ulits.getUser()
        listboxPane.setNonselectableTags("")
        if (userInfo.fbh != null) {
            init()
            listboxPane.setItemRenderer(new JobRenderer())
        }
    }
    def init(){
        jobsList.clear()
        def listJobGroups = quartzScheduler.getJobGroupNames()
        listJobGroups?.each {jobGroup ->
            quartzScheduler.getJobNames(jobGroup)?.each {jobName ->
                def triggers = quartzScheduler.getTriggersOfJob(jobName, jobGroup)
                if (triggers) {
                    triggers.each {trigger ->
                        def currentJob = createJob(jobGroup, jobName, jobsList)
                        currentJob.trigger = trigger
                        currentJob.triggerName = trigger.name
                        currentJob.triggerGroup = trigger.group
                        def state = quartzScheduler.getTriggerState(trigger.name, trigger.group)
                        currentJob.triggerStatus = TriggerState.find {
                            it.value() == state
                        } ?: "UNKNOWN"
                    }
                } else {
                    createJob(jobGroup, jobName, jobsList)
                }
            }
        }
        listboxPane.setModel(new ListModelList(jobsList))
    }

    private def createJob(String jobGroup, String jobName, ArrayList jobsList) {
        def currentJob = [:]
        currentJob.group = jobGroup
        currentJob.name = jobName
        def map = QuartzMonitorJobFactory.jobRuns.get(jobName)
        if (map) currentJob << map
        jobsList.add(currentJob)
        return currentJob
    }

    public class JobRenderer implements  ListitemRenderer{
        void render(Listitem listitem, Object o) {
            listitem.setValue(o)
//            println "triggers=" +triggers
//            println "name="+o.name
//            println "group="+o.group
//            println "trigger="+o.trigger
//            println "triggerName="+o.trigger?.name
//            println "triggerGroup="+o.trigger?.group
//            println quartzScheduler.getTrigger(o.trigger?.name, o.trigger?.group)
            listitem.append{
                listcell(label:o.name.toString())
                listcell(label:o.lastRun?.format("yyyy-MM-dd :HH:mm:ss"))
                listcell(label:o.duration )
                listcell(label:o.trigger?.nextFireTime?.format("yyyy-MM-dd :HH:mm:ss"))
                listcell{
                    if (o.status != 'running') {
                        if (o.trigger) {
                            toolbarbutton(tooltiptext: "停止", image: "/images/icons/stop.png",onClick:{Event ->
                                triggers.put(o.name, quartzScheduler.getTrigger(o.trigger.name, o.trigger.group))
//                                println triggers
                                quartzScheduler.unscheduleJob(o.trigger.name, o.trigger.group)
                                init()
                            })
                            if (o.triggerStatus == TriggerState.PAUSED) {
                                toolbarbutton(tooltiptext: "再继续", image: "/images/icons/resume.png",onClick:{Event ->
                                    quartzScheduler.resumeJob(o.name, o.group)
                                    init()
                                })
                            }else if (o.trigger.mayFireAgain()) {
                                toolbarbutton(tooltiptext: "暂停", image: "/images/icons/pause.png",onClick:{Event ->
                                    quartzScheduler.pauseJob(o.name, o.group)
                                    init()
                                })
                            }
                        }else{
                            toolbarbutton(tooltiptext: "运行", image: "/images/icons/start.png",onClick:{Event ->
//                                println triggers
                                def trigger = triggers.get(o.name)
                                quartzScheduler.scheduleJob(trigger)
                                init()
                            })
                        }
                        toolbarbutton(tooltiptext: "立刻执行", image: "/images/icons/run.png",onClick:{ Event ->
                            jobsList.clear()
                            quartzScheduler.triggerJob(o.name, o.group, null)
                            init()
                            init()
                        })
                    }

                }
            }
        }

    }


    def onClick_tbbfresh(){
        init()
    }

}
