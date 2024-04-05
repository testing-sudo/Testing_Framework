package com.project.pojo;

import com.project.entity.Preferences;
import lombok.Data;

import java.util.List;

@Data
public class ProfilePreferences {

    private String profileName;

    private List<Preferences> preferencesList;
}
