package com.example.lostfound.repo;
import com.example.lostfound.model.Item; import org.springframework.data.jpa.repository.JpaRepository; import org.springframework.data.jpa.repository.Query; import org.springframework.data.repository.query.Param; import java.util.List; import java.util.Optional;
public interface ItemRepository extends JpaRepository<Item,Long>{
 @Query("select i from Item i join fetch i.owner where (:q is null or lower(i.title) like lower(concat('%',:q,'%')) or lower(i.description) like lower(concat('%',:q,'%')) or lower(i.location) like lower(concat('%',:q,'%'))) and (:type is null or i.type=:type) and (:category is null or lower(i.category)=lower(:category)) and (:status is null or i.status=:status) order by i.createdAt desc")
 List<Item> search(@Param("q") String q,@Param("type") Item.ItemType type,@Param("category") String category,@Param("status") Item.ItemStatus status);
 @Query("select i from Item i join fetch i.owner where i.id=:id") Optional<Item> findWithOwnerById(@Param("id") Long id);
}
