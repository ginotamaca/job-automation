package com.company.jobautomation.dto;

import com.company.jobautomation.enums.JobBoard;
import com.company.jobautomation.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {

    private String id;
    private String title;
    private String company;
    private String location;
    private String link;
    private String salary;
    private LocalDateTime publishedDate;
    private JobBoard jobBoard;
    private JobType jobType;
    private boolean viewed;

}
