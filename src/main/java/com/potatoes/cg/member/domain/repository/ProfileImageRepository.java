package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.Dept;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findByMemberCode(Long memberCode);

}
