package com.example.lostfound.model;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
@Entity @Table(name="items")
public class Item {
 public enum ItemType { LOST, FOUND } public enum ItemStatus { LOST, FOUND, CLAIMED, RESOLVED }
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=120) private String title;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=10) private ItemType type;
 @Column(nullable=false,length=80) private String category;
 @Column(nullable=false,length=160) private String location;
 @Column(nullable=false) private LocalDate incidentDate;
 @Column(nullable=false,length=2000) private String description;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=10) private ItemStatus status;
 @Column(length=120) private String contactPhone;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 @Column(nullable=false) private Instant updatedAt=Instant.now();
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="owner_id") private User owner;
 public Item(){} @PreUpdate public void updateTime(){updatedAt=Instant.now();}
 public Long getId(){return id;} public String getTitle(){return title;} public ItemType getType(){return type;}
 public String getCategory(){return category;} public String getLocation(){return location;} public LocalDate getIncidentDate(){return incidentDate;}
 public String getDescription(){return description;} public ItemStatus getStatus(){return status;} public String getContactPhone(){return contactPhone;}
 public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;} public User getOwner(){return owner;}
 public void setTitle(String v){title=v;} public void setType(ItemType v){type=v;} public void setCategory(String v){category=v;}
 public void setLocation(String v){location=v;} public void setIncidentDate(LocalDate v){incidentDate=v;} public void setDescription(String v){description=v;}
 public void setStatus(ItemStatus v){status=v;} public void setContactPhone(String v){contactPhone=v;} public void setOwner(User v){owner=v;}
}
