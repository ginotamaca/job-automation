package com.company.jobautomation.mapper;

import com.company.jobautomation.dto.JobDTO;
import com.company.jobautomation.entity.Job;

import java.util.List;

public interface JobMapper {

    JobDTO toDTO(Job entity);
    List<JobDTO> toDTOs(List<Job> entities);

}
