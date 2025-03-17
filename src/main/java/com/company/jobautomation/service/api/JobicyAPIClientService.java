package com.company.jobautomation.service.api;

import com.company.jobautomation.dto.JobFilter;
import com.company.jobautomation.entity.Job;
import com.company.jobautomation.entity.JobId;
import com.company.jobautomation.enums.JobBoard;
import com.company.jobautomation.enums.JobType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("JOBICY")
public class JobicyAPIClientService extends BaseAPIClientService {

    private static final String API_URL = "https://jobicy.com/api/v2/remote-jobs";

    public JobicyAPIClientService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(API_URL, restTemplate, objectMapper);
    }

    @Override
    public String queryParameters(JobFilter jobFilter) {
        String queryParameters = "";
        if (jobFilter.getKeyword() != null) queryParameters += "tag=" + jobFilter.getKeyword();
        return queryParameters;
    }

    @Override
    public Job toJob(JsonNode node) {

        Job job = new Job();
        job.setId(new JobId(node.get("id").asText(), JobBoard.JOBICY));
        job.setJobBoard(JobBoard.JOBICY);
        job.setTitle(node.get("jobTitle").asText());
        job.setCompany(node.get("companyName").asText());
        job.setLocation(node.has("jobGeo") ? node.get("jobGeo").asText() : "Remote");
        job.setLink(node.get("url").asText());
        job.setSalary(getJobSalary(node));
        job.setJobType(getJobType(node));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        job.setPublishedDate(LocalDateTime.parse(node.get("pubDate").asText(), formatter));
        return job;
    }

    private String getJobSalary(JsonNode node) {

        if (!node.has("salaryCurrency")) return "";

        String currency = node.get("salaryCurrency").asText();
        String annualSalaryMin = node.get("annualSalaryMin").asText();
        String annualSalaryMax = node.get("annualSalaryMax").asText();

        return currency + " " + annualSalaryMin + " - " + currency + " " + annualSalaryMax;
    }

    private JobType getJobType(JsonNode node) {

        JsonNode jobTypeNode = node.get("jobType");
        List<String> jobTypes = new ArrayList<>();

        if (jobTypeNode != null && jobTypeNode.isArray()) {
            for (JsonNode typeNode : jobTypeNode) jobTypes.add(typeNode.asText());
        }

        if (jobTypes.size() == 1) {
            return switch (jobTypes.get(0)) {
                case "full-time" -> JobType.FULL_TIME;
                case "part-time" -> JobType.PART_TIME;
                case "contract" -> JobType.CONTRACT;
                case "internship" -> JobType.INTERNSHIP;
                default -> JobType.UNKNOWN;
            };
        }

        return JobType.MIXED;
    }

}
