package in.gopi.billingsoftware.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.gopi.billingsoftware.entity.OrderItemEntity;

public interface OrderItemEntityRepository extends JpaRepository<OrderItemEntity, Long> {

}
