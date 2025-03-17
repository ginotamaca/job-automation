package com.company.jobautomation.controller;

import com.company.jobautomation.dto.JobDTO;
import com.company.jobautomation.dto.JobFilter;
import com.company.jobautomation.enums.JobBoard;
import com.company.jobautomation.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/fetch/{jobBoard}")
    public ResponseEntity<String> fetchAndSaveJobs(@PathVariable JobBoard jobBoard, @RequestBody JobFilter jobFilter) {
        return ResponseEntity.ok(jobService.fetchAndSaveJobs(jobBoard, jobFilter) + " jobs fetched and saved from " + jobBoard);
    }

    @GetMapping("/{jobBoard}")
    public ResponseEntity<List<JobDTO>> getJobs(@PathVariable JobBoard jobBoard) {
        return ResponseEntity.ok(jobService.getJobs(jobBoard));
    }

}
