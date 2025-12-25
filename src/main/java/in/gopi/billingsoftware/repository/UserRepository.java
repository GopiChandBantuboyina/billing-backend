package in.gopi.billingsoftware.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.gopi.billingsoftware.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserId(String userId);
    
    
    //for checking duplicate email id
    boolean existsByEmail(String email);

} 
