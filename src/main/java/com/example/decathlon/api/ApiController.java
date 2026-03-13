package com.example.decathlon.api;

import com.example.decathlon.core.CompetitionService;
import com.example.decathlon.dto.ScoreReq;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final CompetitionService comp;

    public ApiController(CompetitionService comp) {
        this.comp = comp;
    }

    @PostMapping("/competitors")
    public ResponseEntity<?> add(@RequestBody Map<String, String> body) {
        String name = Optional.ofNullable(body.get("name")).orElse("").trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body("Name is required");
        }
        comp.addCompetitor(name);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/competitors")
    public ResponseEntity<?> rename(@RequestBody Map<String, String> body) {
        String oldName = Optional.ofNullable(body.get("oldName")).orElse("").trim();
        String newName = Optional.ofNullable(body.get("newName")).orElse("").trim();

        if (oldName.isEmpty() || newName.isEmpty()) {
            return ResponseEntity.badRequest().body("Both old name and new name are required");
        }

        boolean renamed = comp.renameCompetitor(oldName, newName);
        if (!renamed) {
            return ResponseEntity.badRequest().body("Could not edit competitor");
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/score")
    public Map<String, Integer> score(@RequestBody ScoreReq r) {
        int pts = comp.score(r.name(), r.event(), r.raw());
        return Map.of("points", pts);
    }

    @GetMapping("/standings")
    public List<Map<String, Object>> standings() {
        return comp.standings();
    }

    @GetMapping(value = "/export.csv", produces = MediaType.TEXT_PLAIN_VALUE)
    public String export() {
        return comp.exportCsv();
    }
}