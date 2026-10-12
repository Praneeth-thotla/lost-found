package com.example.lostfound.api;
import com.example.lostfound.model.*; import com.example.lostfound.repo.*; import jakarta.validation.Valid; import jakarta.validation.constraints.*; import org.springframework.http.HttpStatus; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException; import java.time.LocalDate; import java.time.Instant; import java.util.*;
@RestController @RequestMapping("/api/items") public class ItemController {
 public record ItemRequest(@NotBlank @Size(max=120) String title,@NotNull Item.ItemType type,@NotBlank @Size(max=80) String category,@NotBlank @Size(max=160) String location,@NotNull LocalDate incidentDate,@NotBlank @Size(max=2000) String description,Item.ItemStatus status,@Size(max=120) String contactPhone){}
 public record ItemDto(Long id,String title,Item.ItemType type,String category,String location,LocalDate incidentDate,String description,Item.ItemStatus status,String contactPhone,String ownerName,String ownerEmail,Long ownerId,Instant createdAt){}
 private final ItemRepository items;private final UserRepository users;
 public ItemController(ItemRepository i,UserRepository u){items=i;users=u;}
 @GetMapping public List<ItemDto> list(@RequestParam(required=false) String q,@RequestParam(required=false) Item.ItemType type,@RequestParam(required=false) String category,@RequestParam(required=false) Item.ItemStatus status){
  return items.search(blank(q),type,blank(category),status).stream().map(this::dto).toList();
 }
 @GetMapping("/{id}") public ItemDto get(@PathVariable Long id){return dto(items.findWithOwnerById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Item not found")));}
 @PostMapping public ItemDto create(@Valid @RequestBody ItemRequest r,Authentication a){Item i=new Item();apply(i,r);i.setOwner(current(a));if(i.getStatus()==null)i.setStatus(i.getType()==Item.ItemType.LOST?Item.ItemStatus.LOST:Item.ItemStatus.FOUND);return dto(items.save(i));}
 @PutMapping("/{id}") public ItemDto update(@PathVariable Long id,@Valid @RequestBody ItemRequest r,Authentication a){Item i=owned(id,a);apply(i,r);if(i.getStatus()==null)i.setStatus(i.getType()==Item.ItemType.LOST?Item.ItemStatus.LOST:Item.ItemStatus.FOUND);return dto(items.save(i));}
 @DeleteMapping("/{id}") public Map<String,String> delete(@PathVariable Long id,Authentication a){items.delete(owned(id,a));return Map.of("message","Deleted");}
 private void apply(Item i,ItemRequest r){i.setTitle(r.title().trim());i.setType(r.type());i.setCategory(r.category().trim());i.setLocation(r.location().trim());i.setIncidentDate(r.incidentDate());i.setDescription(r.description().trim());i.setContactPhone(r.contactPhone());if(r.status()!=null)i.setStatus(r.status());}
 private User current(Authentication a){return users.findByEmail(a.getName()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED));}
 private Item owned(Long id,Authentication a){Item i=items.findWithOwnerById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Item not found"));if(!i.getOwner().getEmail().equalsIgnoreCase(a.getName()))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You can manage only your own reports");return i;}
 private ItemDto dto(Item i){return new ItemDto(i.getId(),i.getTitle(),i.getType(),i.getCategory(),i.getLocation(),i.getIncidentDate(),i.getDescription(),i.getStatus(),i.getContactPhone(),i.getOwner().getName(),i.getOwner().getEmail(),i.getOwner().getId(),i.getCreatedAt());}
 private String blank(String s){return s==null||s.isBlank()?null:s.trim();}
}
