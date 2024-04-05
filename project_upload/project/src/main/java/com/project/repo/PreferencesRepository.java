package com.project.repo;


import com.project.entity.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PreferencesRepository extends JpaRepository<Preferences, Integer> {


    List<Preferences> findByUserNameAndProfileName(String username, String profileName);

    List<Preferences> findByUserNameAndProfileNameAndPrefName(String username, String profileName, String prefName);

    long countByProfileName(String profileName);

    void deleteByUserNameAndProfileName(String username, String profileName);

    void deleteByUserNameAndProfileNameAndPrefName(String username, String profileName, String prefName);
}
