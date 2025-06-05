package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.services.rating.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

}
