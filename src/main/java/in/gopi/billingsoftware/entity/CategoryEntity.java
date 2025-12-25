package in.gopi.billingsoftware.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_category")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {
	
	//treat it as unique id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String categoryId;
	
	@Column(unique = true)
	private String name;
	private String description;
	private String bgColor;
	private String imageUrl;
	
	@CreationTimestamp
	@Column(updatable = false) //In update operation not going to change
	private Timestamp createdAt; // logging purpose
	
	@CreationTimestamp
	private Timestamp updatedAt;

	
}
