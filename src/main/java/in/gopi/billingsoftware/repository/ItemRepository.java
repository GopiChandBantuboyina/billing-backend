package in.gopi.billingsoftware.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.gopi.billingsoftware.entity.ItemEntity;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long>{
	
	Optional<ItemEntity> findByItemId(String id);
	Integer countByCategoryId(Long id);

}
