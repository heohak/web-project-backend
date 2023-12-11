package com.taldate.backend.match.controller;

import com.taldate.backend.match.dto.MatchDTO;
import com.taldate.backend.match.service.MatchService;
import com.taldate.backend.profile.dto.ProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @GetMapping
    public List<ProfileDTO> getAllMatches() {
        return matchService.getAllMatches();
    }

    @PostMapping
    public void match(@RequestBody MatchDTO dto) {
        matchService.match(dto);
    }
}
