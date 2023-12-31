package ra.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "coupons")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Coupon {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String coupon;
	
	private double percent;
	
	private int stock;
	
	@Column(name = "start_date")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	
	@Column(name = "end_date")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endDate;
	
	private boolean status;
	
	@OneToMany( fetch = FetchType.LAZY, mappedBy = "coupon")
	@JsonIgnore
	private List<Orders> orders = new ArrayList<>();
	
}
