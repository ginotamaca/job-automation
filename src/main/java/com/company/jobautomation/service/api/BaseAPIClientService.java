package com.company.jobautomation.service.api;

import com.company.jobautomation.dto.JobFilter;
import com.company.jobautomation.entity.Job;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseAPIClientService implements APIClientService {

    private String apiUrl;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    protected BaseAPIClientService() {}

    protected BaseAPIClientService(String apiUrl, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Job> fetchJobs(JobFilter jobFilter) {

        List<Job> jobs = new ArrayList<>();

        try {

            String url = apiUrl;
            if (jobFilter != null) url = url + "?" + queryParameters(jobFilter);

            log.info("URL to fetch jobs: {}", url);

            String json = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode jobsArray = rootNode.get("jobs");

            if (jobsArray != null && jobsArray.isArray()) {
                for (JsonNode jobNode : jobsArray) jobs.add(toJob(jobNode));
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return jobs;
    }

    protected abstract Job toJob(Object object);

    protected abstract String queryParameters(JobFilter jobFilter);

}
