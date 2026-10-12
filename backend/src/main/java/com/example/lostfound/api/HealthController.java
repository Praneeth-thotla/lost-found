package com.example.lostfound.api;
import org.springframework.web.bind.annotation.*;import java.util.Map;
@RestController public class HealthController{@GetMapping("/api/health") public Map<String,String> health(){return Map.of("status","ok","service","lost-found-portal");}}
