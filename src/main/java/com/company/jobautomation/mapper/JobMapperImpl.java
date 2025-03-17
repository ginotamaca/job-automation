package com.company.jobautomation.mapper;

import com.company.jobautomation.dto.JobDTO;
import com.company.jobautomation.entity.Job;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobMapperImpl implements JobMapper {

    @Override
    public JobDTO toDTO(Job entity) {
        if (entity == null) return null;
        JobDTO dto = new JobDTO();
        dto.setId(entity.getId().getJobId());
        dto.setTitle(entity.getTitle());
        dto.setCompany(entity.getCompany());
        dto.setLocation(entity.getLocation());
        dto.setLink(entity.getLink());
        dto.setSalary(entity.getSalary());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setJobBoard(entity.getJobBoard());
        dto.setJobType(entity.getJobType());
        dto.setViewed(entity.isViewed());
        return dto;
    }

    @Override
    public List<JobDTO> toDTOs(List<Job> entities) {
        return entities.stream().map(this::toDTO).toList();
    }

}
