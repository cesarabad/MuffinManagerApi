package com.muffinmanager.api.muffinmanagerapi.model.User.database.stats;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Immutable
@Table(name = "userstats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatsEntity {
    @Id
    @Column(name = "userid")
    private int user;

    @Column(name = "name")
    private String name;

    @Column(name = "totalentries")
    private long totalEntries;

    @Column(name = "totalassigneds")
    private long totalAssigneds;

    @Column(name = "totaladjustments")
    private long totalAdjustments;

    @Column(name = "totalreserveds")
    private long totalReserveds;

    @Column(name = "totalchecked")
    private long totalChecked;
}
