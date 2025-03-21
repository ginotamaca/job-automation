package com.company.jobautomation.service.api;

import com.company.jobautomation.dto.JobFilter;
import com.company.jobautomation.entity.Job;

import java.util.List;

public interface APIClientService {
    List<Job> fetchJobs(JobFilter jobFilter);
}
