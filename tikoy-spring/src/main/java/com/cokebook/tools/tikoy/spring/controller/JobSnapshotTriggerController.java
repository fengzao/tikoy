package com.cokebook.tools.tikoy.spring.controller;

import com.cokebook.tools.tikoy.container.JobSnapshotRunProps;
import com.cokebook.tools.tikoy.container.JobSnapshotTrigger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2025/2/3
 */
@RestController
@RequestMapping("/job-snapshot-trigger")
public class JobSnapshotTriggerController {

    private final JobSnapshotTrigger trigger;

    public JobSnapshotTriggerController(JobSnapshotTrigger trigger) {
        this.trigger = trigger;
    }

    @PostMapping("/start")
    public String start(@RequestBody JobSnapshotRunProps config) {
        trigger.start(config);
        return "success";
    }

    @PostMapping("/stop")
    public String stop(@RequestBody JobSnapshotRunProps config) {
        trigger.stop(config);
        return "success";
    }

}
