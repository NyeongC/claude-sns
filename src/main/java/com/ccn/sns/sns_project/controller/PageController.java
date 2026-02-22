package com.ccn.sns.sns_project.controller;

import com.ccn.sns.sns_project.config.auth.AuthUser;
import com.ccn.sns.sns_project.controller.dto.FollowCountResponse;
import com.ccn.sns.sns_project.controller.dto.PostResponse;
import com.ccn.sns.sns_project.domain.follow.FollowService;
import com.ccn.sns.sns_project.domain.post.PostService;
import com.ccn.sns.sns_project.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final PostService postService;
    private final FollowService followService;

    @GetMapping("/")
    public String index(@AuthUser User user,
                        @RequestParam(defaultValue = "latest") String sort,
                        Model model) {
        List<PostResponse> posts = postService.getPosts(user.getUsername(), sort);
        model.addAttribute("username", user.getUsername());
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
                          @AuthUser User user,
                          Model model) {
        FollowCountResponse followCount = followService.getFollowCount(username);
        List<PostResponse> posts = postService.getPostsByUser(user.getUsername(), username);
        boolean isFollowing = followService.isFollowing(user.getUsername(), username);

        model.addAttribute("currentUsername", user.getUsername());
        model.addAttribute("profileUsername", username);
        model.addAttribute("followCount", followCount);
        model.addAttribute("posts", posts);
        model.addAttribute("isFollowing", isFollowing);
        return "profile";
    }
}
