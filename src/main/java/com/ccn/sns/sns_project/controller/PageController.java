package com.ccn.sns.sns_project.controller;

import com.ccn.sns.sns_project.controller.dto.FollowCountResponse;
import com.ccn.sns.sns_project.controller.dto.PostResponse;
import com.ccn.sns.sns_project.domain.follow.FollowService;
import com.ccn.sns.sns_project.domain.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final PostService postService;
    private final FollowService followService;

    @GetMapping("/")
    public String index(Principal principal,
                        @RequestParam(defaultValue = "latest") String sort,
                        Model model) {
        List<PostResponse> posts = postService.getPosts(principal.getName(), sort);
        model.addAttribute("username", principal.getName());
        model.addAttribute("posts", posts);
        model.addAttribute("sort", sort);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable String username,
                          Principal principal,
                          Model model) {
        FollowCountResponse followCount = followService.getFollowCount(username);
        List<PostResponse> posts = postService.getPostsByUser(principal.getName(), username);
        boolean isFollowing = followService.isFollowing(principal.getName(), username);

        model.addAttribute("currentUsername", principal.getName());
        model.addAttribute("profileUsername", username);
        model.addAttribute("followCount", followCount);
        model.addAttribute("posts", posts);
        model.addAttribute("isFollowing", isFollowing);
        return "profile";
    }
}
