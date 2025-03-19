package com.company.jobautomation.service.api;

import com.company.jobautomation.dto.JobFilter;
import com.company.jobautomation.entity.Job;
import com.company.jobautomation.entity.JobId;
import com.company.jobautomation.enums.JobBoard;
import com.company.jobautomation.enums.JobType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("WWR")
@Slf4j
@RequiredArgsConstructor
public class WWRAPIClientService extends BaseAPIClientService {

    private static final String RSS_URL = "https://weworkremotely.com/categories/remote-programming-jobs.rss";
    private final ObjectMapper mapper;

    @Override
    public List<Job> fetchJobs(JobFilter jobFilter) {

        List<Job> jobs = new ArrayList<>();

        try {

            URL url = new URL(RSS_URL);

            // Convert XML to JSON using XmlMapper
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode root = xmlMapper.readTree(url.openStream());

            // Convert JSON and parse using JsonNode
            String json = mapper.writeValueAsString(root);
            JsonNode jsonNode = mapper.readTree(json);
            JsonNode items = jsonNode.path("channel").path("item");

            if (items.isArray()) {

                String keyword = queryParameters(jobFilter);
                for (JsonNode item : items) {
                    String description = item.path("description").asText().toLowerCase();
                    if (!description.contains(keyword)) continue;
                    jobs.add(toJob(item));
                }
            }


        } catch (Exception e) {
            log.error("Error fetching jobs from WWR API", e);
        }

        return jobs;
    }

    @Override
    protected Job toJob(Object object) {

        JsonNode node = (JsonNode) object;
        Job job = new Job();
        job.setId(new JobId(node.path("guid").asText(), JobBoard.WWR));
        job.setJobBoard(JobBoard.WWR);
        job.setTitle(node.path("title").asText());
        job.setCompany("");
        job.setLocation(node.path("region").asText());
        job.setLink(node.path("link").asText());
        job.setSalary("");
        job.setJobType(JobType.UNKNOWN);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(node.path("pubDate").asText(), formatter);
        job.setPublishedDate(zonedDateTime.toLocalDateTime());

        return job;
    }

    @Override
    protected String queryParameters(JobFilter jobFilter) {
        if (jobFilter.getKeyword() != null) return jobFilter.getKeyword();
        return "";
    }

}
