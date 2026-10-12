package com.example.lostfound.api;
import com.example.lostfound.model.User; import com.example.lostfound.repo.UserRepository; import com.example.lostfound.security.JwtService; import jakarta.validation.Valid; import jakarta.validation.constraints.*; import org.springframework.http.HttpStatus; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException; import java.util.Map;
@RestController @RequestMapping("/api/auth") public class AuthController {
 public record Register(@NotBlank @Size(max=100) String name,@NotBlank @Email @Size(max=190) String email,@NotBlank @Size(min=8,max=72) String password){}
 public record Login(@NotBlank @Email String email,@NotBlank String password){}
 private final UserRepository users;private final PasswordEncoder encoder;private final JwtService jwt;
 public AuthController(UserRepository u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 @PostMapping("/register") public Map<String,Object> register(@Valid @RequestBody Register r){
  String email=r.email().trim().toLowerCase();if(users.existsByEmail(email))throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");
  User u=users.save(new User(r.name().trim(),email,encoder.encode(r.password())));return result(u);
 }
 @PostMapping("/login") public Map<String,Object> login(@Valid @RequestBody Login r){
  User u=users.findByEmail(r.email().trim().toLowerCase()).filter(x->encoder.matches(r.password(),x.getPasswordHash())).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password"));return result(u);
 }
 private Map<String,Object> result(User u){return Map.of("token",jwt.generate(u.getEmail()),"user",Map.of("id",u.getId(),"name",u.getName(),"email",u.getEmail()));}
}
