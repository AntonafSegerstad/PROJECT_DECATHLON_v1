package com.example.decathlon.core;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CompetitionService {
    private final ScoringService scoring;

    public CompetitionService(ScoringService scoring) {
        this.scoring = scoring;
    }

    public static class Competitor {
        public String name;
        public final Map<String, Integer> points = new ConcurrentHashMap<>();

        public Competitor(String name) {
            this.name = name;
        }

        public int total() {
            return points.values().stream().mapToInt(i -> i).sum();
        }
    }

    private final Map<String, Competitor> competitors = new LinkedHashMap<>();

    public synchronized void addCompetitor(String name) {
        if (!competitors.containsKey(name)) {
            competitors.put(name, new Competitor(name));
        }
    }

    public synchronized boolean renameCompetitor(String oldName, String newName) {
        if (!competitors.containsKey(oldName) || competitors.containsKey(newName)) {
            return false;
        }

        Competitor c = competitors.remove(oldName);
        c.name = newName;
        competitors.put(newName, c);
        return true;
    }

    public synchronized int score(String name, String eventId, double raw) {
        Competitor c = competitors.computeIfAbsent(name, Competitor::new);
        int pts = scoring.score(eventId, raw);
        c.points.put(eventId, pts);
        return pts;
    }

    public synchronized List<Map<String, Object>> standings() {
        List<Competitor> sorted = competitors.values().stream()
                .sorted(Comparator.comparingInt(Competitor::total).reversed())
                .collect(Collectors.toList());

        List<Map<String, Object>> result = new ArrayList<>();
        int placement = 1;

        for (Competitor c : sorted) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("placement", placement++);
            m.put("name", c.name);
            m.put("scores", new LinkedHashMap<>(c.points));
            m.put("total", c.total());
            result.add(m);
        }

        return result;
    }

    public synchronized String exportCsv() {
        Set<String> eventIds = new LinkedHashSet<>();
        competitors.values().forEach(c -> eventIds.addAll(c.points.keySet()));
        List<String> header = new ArrayList<>();
        header.add("Placement");
        header.add("Name");
        header.addAll(eventIds);
        header.add("Total");

        StringBuilder sb = new StringBuilder();
        sb.append(String.join(",", header)).append("\n");

        List<Competitor> sorted = competitors.values().stream()
                .sorted(Comparator.comparingInt(Competitor::total).reversed())
                .collect(Collectors.toList());

        int placement = 1;
        for (Competitor c : sorted) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(placement++));
            row.add(c.name);
            int sum = 0;
            for (String ev : eventIds) {
                Integer p = c.points.get(ev);
                row.add(p == null ? "" : String.valueOf(p));
                if (p != null) {
                    sum += p;
                }
            }
            row.add(String.valueOf(sum));
            sb.append(String.join(",", row)).append("\n");
        }

        return sb.toString();
    }
}