package com.potatoes.cg.member.dto;

import lombok.*;

import javax.persistence.Entity;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @ToString
@NoArgsConstructor
public class MailSendDTO {

    private String address;
    private String title;
    private String message;

}
