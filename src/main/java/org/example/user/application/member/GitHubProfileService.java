//package org.example.user.application.member;
//
//import lombok.RequiredArgsConstructor;
//import org.example.user.domain.entity.member.GitHubProfile;
//import org.example.user.repository.member.GitHubProfileRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class GitHubProfileService {
//
//    private final GitHubProfileRepository gitHubProfileRepository;
//
//    public GitHubProfile save(GitHubProfile gitHubProfile) {
//        return gitHubProfileRepository.save(gitHubProfile);
//    }
//
//    public GitHubProfile findById(Long id) {
//        return gitHubProfileRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
//    }
//
//    public GitHubProfile findByUserName(String userName) {
//        return gitHubProfileRepository.findByUserName(userName)
//                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
//    }
//
//    public List<GitHubProfile> findAll() {
//        return gitHubProfileRepository.findAll();
//    }
//
//}
