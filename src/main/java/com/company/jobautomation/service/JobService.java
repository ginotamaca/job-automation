package com.company.jobautomation.service;

import com.company.jobautomation.dto.JobDTO;
import com.company.jobautomation.dto.JobFilter;
import com.company.jobautomation.enums.JobBoard;

import java.util.List;

public interface JobService {

    Integer fetchAndSaveJobs(JobBoard jobBoard, JobFilter jobFilter);
    List<JobDTO> getJobs();

}
