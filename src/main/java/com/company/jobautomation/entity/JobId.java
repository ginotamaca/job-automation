package com.company.jobautomation.entity;

import com.company.jobautomation.enums.JobBoard;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
public class JobId implements Serializable {

    private String jobId;

    @Enumerated(EnumType.STRING)
    private JobBoard jobBoard;

    public JobId() {}

    public JobId(String jobId, JobBoard jobBoard) {
        this.jobId = jobId;
        this.jobBoard = jobBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobId jobId1 = (JobId) o;
        return Objects.equals(jobId, jobId1.jobId) && jobBoard == jobId1.jobBoard;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, jobBoard);
    }
}
