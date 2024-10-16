package com.kdigital.SecondProject.controller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kdigital.SecondProject.dto.AccidentStatusDTO;
import com.kdigital.SecondProject.dto.UserDTO;
import com.kdigital.SecondProject.dto.VoyageDTO;
import com.kdigital.SecondProject.service.AISService;
import com.kdigital.SecondProject.service.AccidentStatusService;
import com.kdigital.SecondProject.service.VoyageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
	final VoyageService voyageService;
	final AISService aisService;
	final AccidentStatusService asService;
	/**
	 * 첫 화면 요청
	 * @return "main.html"
	 * */
	@GetMapping({"","/"})
	public String main(
			@AuthenticationPrincipal  UserDTO loginUser, //인증받은 사용자가 있다면 그 정보를 담아옴
			@RequestParam(name="ship", defaultValue="집갈래") String shipInfo, //검색버튼 클릭 시
			Model model
			) {
		// 항해 정보 저장
		VoyageDTO voyageDTO = new VoyageDTO();
		log.info("(service) 데이터를 입력 받기 전의 항해 정보: {}",voyageDTO.toString());
		// 인증을 받은 사용자라면 그 이름 저장 
		if(loginUser!=null) {
			model.addAttribute("loginName", loginUser.getUserId());
		}
		
		// 검색을 통해 접근했는지 여부 파악
			// 검색하지 않은 접근시, 바로 메인
		if(shipInfo.equals("집갈래")) {
			model.addAttribute("search", 0); //검색 하지 않고 접근함.
			return "main";
		}
			// 검색을 통해 접근
		VoyageDTO temp = voyageService.selectVoyageWithCallSign(shipInfo);
		log.info("(service) call sign으로 찾아온 항해 정보: {}",temp);
		if(temp!=null) voyageDTO = temp;
		
		temp = voyageService.selectVoyageWithMmsi(shipInfo);
		log.info("(service) MMSI로 찾아온 항해 정보: {}",temp);
		if(temp!=null) voyageDTO = temp;
		
		temp = voyageService.selectVoyageWithImo(shipInfo);
		log.info("(service) IMO로 찾아온 항해 정보: {}",temp);
		if(temp!=null) voyageDTO = temp;
		
		// 해당 선박 또는 항해가 없는 경우
		if(voyageDTO.getVNumber()==null) {
			model.addAttribute("search", 2);
			return "main";
		}
		
		// 항해가 제대로 존재함
		model.addAttribute("search", 1);
		// 추출된 항해 전달
		model.addAttribute("voyage", voyageDTO);
		
		// 항해 진행률 생성 및 전달
		String voyagePer = getVoyagePer(voyageDTO.getVNumber());
		model.addAttribute("voyagePer", voyagePer);
		
		// 목적항 사고 현황 전달
		AccidentStatusDTO asDTO = asService.getAccidentStatusByPortCode(voyageDTO.getPort().getPortCode()).get(0);
		model.addAttribute("accidentStatus", asDTO);
		
		return "main";
	}

	private String getVoyagePer(Long vNumber) {
		LocalDateTime arrivalDate = voyageService.selectOne(vNumber).getArrivalDate();
		LocalDateTime departureDate = voyageService.selectOne(vNumber).getDepartureDate();
		LocalDateTime currentSignal = aisService.currentAISsignal(vNumber).getSignalDate();
		
		long totalVoyageMinutes = Duration.between(departureDate, arrivalDate).toMinutes();
		long untilTodayMinutes = Duration.between(currentSignal, arrivalDate).toMinutes();
		
		//현재 항해일 수 / 총 항해일 수 * 100 = 항해 진행률
		double voyageProgress = ((double) untilTodayMinutes / totalVoyageMinutes) * 100;
		log.info("현재 항해에 대한 항해 진행률: {}",voyageProgress);
		return String.format("%.2f", voyageProgress);
	}
}
