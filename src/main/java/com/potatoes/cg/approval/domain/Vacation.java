package com.potatoes.cg.approval.domain;

import com.potatoes.cg.approval.domain.type.vacationType.VacationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_vacation")
@Getter
@NoArgsConstructor
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacationCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacationType vacationType;

    @DateTimeFormat(pattern = "YYYY-MM-DD")
    @Column
    private LocalDate vacationStartDate;

    @DateTimeFormat(pattern = "YYYY-MM-DD")
    @Column
    private LocalDate vacationEndDate;

    @Column
    private String vacationBody;

    @Column
    private String vacationEmergencyPhone;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "approvalCode")
    private Approval approval;

    public Vacation(VacationType vacationType, LocalDate vacationStartDate,
                    LocalDate vacationEndDate, String vacationBody,
                    String vacationEmergencyPhone, Approval approval) {

        this.vacationType = vacationType;
        this.vacationStartDate = vacationStartDate;
        this.vacationEndDate = vacationEndDate;
        this.vacationBody = vacationBody;
        this.vacationEmergencyPhone = vacationEmergencyPhone;
        this.approval = approval;
    }


    public static Vacation of(VacationType vacationType, LocalDate vacationStartDate,
                              LocalDate vacationEndDate, String vacationBody,
                              String vacationEmergencyPhone,Approval approval) {

        return new Vacation(
                vacationType,
                vacationStartDate,
                vacationEndDate,
                vacationBody,
                vacationEmergencyPhone,
                approval

        );
    }
}
