package com.kdigital.SecondProject.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kdigital.SecondProject.dto.VoyageDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder

@Entity
@Table(name="voyage")
public class VoyageEntity {
	@Id
	@Column(name="v_number")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vNumber;
	
//	@Column(name="call_sign")
//	private String callSign;
	
	@ManyToOne
    @JoinColumn(name = "call_sign", referencedColumnName = "call_sign")  // 외래 키 매핑
    private ShipEntity ship;  // 외래 키로 ShipEntity 참조
	
	@Column(name="departure_date")
	private LocalDateTime departureDate;
	
	@Column(name="arrival_date")
	private LocalDateTime arrivalDate;
	
	@Column(name="departure")
	private String departure;
	
	// 목적지 정보 코드가 존재해야 테스트 해볼 수 있음
	// @ManyToOne
	// @JoinColumn(name = "destination", referencedColumnName = "destination")  // 외래 키 매핑
	@Column(name="destination")
	private String destination; //private PortEntity port;
	
	@Column(name="on_boarding")
	private String onBoarding;
	
	@Column(name="extra_tonnage")
	private int extraTonnage;
	
	@Column(name="entry_exit_fee")
	private int entryExitFee;
	
	@Column(name="berthing_fee")
	private int berthingFee;
	
	@Column(name="anchorage_fee")
	private int anchorageFee;
	
	@Column(name="security_fee")
	private int securityFee;
	
	
	@OneToMany(mappedBy = "voyage", fetch = FetchType.LAZY)
	@JsonIgnore  // JSON 직렬화에서 제외
	private List<AISEntity> aisDataList; // AISData와의 관계 설정
	
	public VoyageEntity(String destination, String onBoarding, int extraTonnage, int entryExitFee, int berthingFee, int anchorageFee, int securityFee) {
	    this.destination = destination;
	    this.onBoarding = onBoarding;
	    this.extraTonnage = extraTonnage;
	    this.entryExitFee = entryExitFee;
	    this.berthingFee = berthingFee;
	    this.anchorageFee = anchorageFee;
	    this.securityFee = securityFee;
	}
	
	
	//dto->entity
	public static VoyageEntity toEntity(VoyageDTO dto) {
		return VoyageEntity.builder()
		.vNumber(dto.getVNumber())
		//.callSign(dto.getCallSign()) : 외래키 참조를 넣지 않았을 때
		.ship(dto.getShip()) // 외래키 참조 시, ship 객체를 통째로 가져옴
		.departureDate(dto.getDepartureDate())
		.arrivalDate(dto.getArrivalDate())
		.departure(dto.getDeparture())
		.destination(dto.getDestination())  //port(dto.getPort())
		.onBoarding(dto.getOnBoarding())
		.extraTonnage(dto.getExtraTonnage())
		.entryExitFee(dto.getEntryExitFee())
		.berthingFee(dto.getBerthingFee())
		.anchorageFee(dto.getAnchorageFee())
		.securityFee(dto.getSecurityFee())
		.build();
	}
}
