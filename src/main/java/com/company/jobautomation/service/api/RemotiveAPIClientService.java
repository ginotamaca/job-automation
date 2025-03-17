package com.company.jobautomation.service.api;

import com.company.jobautomation.entity.Job;
import com.company.jobautomation.entity.JobId;
import com.company.jobautomation.enums.JobBoard;
import com.company.jobautomation.enums.JobType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("REMOTIVE")
@RequiredArgsConstructor
@Slf4j
public class RemotiveAPIClientService implements APIClientService {

    private static final String API_URL = "https://remotive.com/api/remote-jobs?search=java";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<Job> fetchJobs() {

        List<Job> jobs = new ArrayList<>();

        try {

            String json = restTemplate.getForObject(API_URL, String.class);
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

    private JobType getJobType(JsonNode node) {

        String jobType = node.get("job_type").asText();

        return switch (jobType) {
            case "full_time" -> JobType.FULL_TIME;
            case "part_time" -> JobType.PART_TIME;
            case "contract" -> JobType.CONTRACT;
            case "temporary" -> JobType.TEMPORARY;
            case "freelance" -> JobType.FREELANCE;
            case "internship" -> JobType.INTERNSHIP;
            default -> JobType.OTHER;
        };

    }

    private Job toJob(JsonNode node) {
        Job job = new Job();
        job.setId(new JobId(node.get("id").asText(), JobBoard.REMOTIVE));
        job.setJobBoard(JobBoard.REMOTIVE);
        job.setTitle(node.get("title").asText());
        job.setCompany(node.get("company_name").asText());
        job.setLocation(node.has("candidate_required_location") ? node.get("candidate_required_location").asText() : "Remote");
        job.setLink(node.get("url").asText());
        job.setSalary(node.get("salary").asText());
        job.setJobType(getJobType(node));
        job.setPublishedDate(LocalDateTime.parse(node.get("publication_date").asText()));
        return job;
    }

}
