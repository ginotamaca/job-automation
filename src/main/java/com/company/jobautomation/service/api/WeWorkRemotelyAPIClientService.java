package com.company.jobautomation.service.api;

import com.company.jobautomation.entity.Job;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("WWR")
public class WeWorkRemotelyAPIClientService implements APIClientService {

    @Override
    public List<Job> fetchJobs() {
        return List.of();
    }
}
