package com.company.jobautomation.service;

import com.company.jobautomation.dto.JobDTO;
import com.company.jobautomation.dto.JobFilter;
import com.company.jobautomation.entity.Job;
import com.company.jobautomation.enums.JobBoard;
import com.company.jobautomation.mapper.JobMapper;
import com.company.jobautomation.repository.JobRepository;
import com.company.jobautomation.service.api.APIClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainJobService implements JobService {

    private static final int SAVE_JOB_LIMIT = 20;

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final Map<String, APIClientService> apiClientServices;

    @Override
    @Transactional
    public Integer fetchAndSaveJobs(JobBoard jobBoard, JobFilter jobFilter) {

        APIClientService apiClientService = apiClientServices.get(jobBoard.toString());

        if (apiClientService == null) {
            throw new IllegalArgumentException("Invalid job board: " + jobBoard);
        }

        log.info("Fetching jobs from {} API", jobBoard);
        return saveJobs(apiClientService.fetchJobs(jobFilter));
    }

    @Override
    public List<JobDTO> getJobs(JobBoard jobBoard) {
        List<Job> jobs = jobRepository.findByJobBoardAndViewedFalse(jobBoard);
        return jobMapper.toDTOs(jobs);
    }

    private int saveJobs(List<Job> jobs) {
        List<Job> uniqueJobs = jobs.stream()
                .filter(job -> !jobRepository.existsById(job.getId()))
                .limit(SAVE_JOB_LIMIT)
                .toList();
        jobRepository.saveAll(uniqueJobs);
        return uniqueJobs.size();
    }

}
