package com.project.repo;

import com.project.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    List<Profile> findByUserName(String userName);

    Profile findByProfileName(String profileName);

    void deleteByUserNameAndProfileName(String userName, String profileName);
}
