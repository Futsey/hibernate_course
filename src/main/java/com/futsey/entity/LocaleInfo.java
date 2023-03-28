package com.futsey.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public class LocaleInfo {

    private String lang;
    private String description;
}
