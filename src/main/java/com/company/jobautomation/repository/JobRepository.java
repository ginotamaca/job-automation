package com.company.jobautomation.repository;

import com.company.jobautomation.entity.Job;
import com.company.jobautomation.entity.JobId;
import com.company.jobautomation.enums.JobBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, JobId> {

    boolean existsById(JobId id);
    List<Job> findByJobBoardAndViewedFalse(JobBoard jobBoard);

}
