package com.kdigital.SecondProject.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kdigital.SecondProject.dto.PortDTO;
import com.kdigital.SecondProject.dto.ShipDTO;
import com.kdigital.SecondProject.dto.VoyageDTO;
import com.kdigital.SecondProject.entity.PortEntity;
import com.kdigital.SecondProject.service.PortService;
import com.kdigital.SecondProject.service.ShipService;
import com.kdigital.SecondProject.service.VoyageService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor

/**
 * 계산기 정보 화면으로 이동
 * */

public class CalcController {
	
final private VoyageService voyageService;
final private ShipService shipService; 
final private PortService portService;
	
	/**
	 * 상단 메뉴바를 통한 계산기 화면 요청
	 * -> 비회원/정회원 모두 전달되는 기본 정보 동일(0)
	 * @param model
	 * @param session
	 * @return
	 */

	@GetMapping("port/calcdetail")
	public String hearderPortD(Model model, HttpSession session) {
		// 기본값 설정
		model.addAttribute("portName", "국내항");
		model.addAttribute("tonnage", 0);
		
		// 출항 일시, 입항 일시 yyyy-MM-dd 형태 변환, 설정
		LocalDate today = LocalDate.now();
		String todayformat = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		model.addAttribute("importDate", todayformat);
		model.addAttribute("exportDate", todayformat);
			
		// 작업 시간, 대기 시간 설정
	    int defaultHour = 0;
	    int defaultMinute = 0;
	    
	    model.addAttribute("workingHour", defaultHour);
	    model.addAttribute("workingMinute", defaultMinute);
	    model.addAttribute("waitingHour", defaultHour);
	    model.addAttribute("waitingMinute", defaultMinute);
				
		model.addAttribute("shipName", "-");
		model.addAttribute("callSign", "-");
		model.addAttribute("result", 0);
		model.addAttribute("fee", "-");
		model.addAttribute("portion", "-");
		
		// 저장 버튼 상태 플래그 설정
	    model.addAttribute("isSaveEnabled", false);


		//기존 세션 확인 및 값 전달
		if(session.getAttributeNames().hasMoreElements()) {
		   model.addAttribute("session_port",(String) session.getAttribute("session_port"));
		   model.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
		}
	    
		return "pages/calculator";
	}
	
	
	
	/**
	 * 메인 화면을 통한 접속
	 * -> 정회원 로그인 시에만 가능
	 * @param callSign
	 * @param model
	 * @return
	 */
	@Transactional
	@GetMapping("calc/calcdetail")
	public String mainPortD(@RequestParam("callSign") String callSign, Model model, HttpSession session) {
		// call sign으로 기준 항해, 선박 정보 조회
		VoyageDTO voyage = voyageService.selectVoyageWithCallSign(callSign);
		ShipDTO ship = shipService.selectOneShip(callSign);
		
		// portCode로 항구 정보 조회
		PortEntity portEntity = voyage.getPort();
		String portCode = portEntity.getPortCode();
		PortDTO port = portService.selectPortByPortCode(portCode);
		
		// 입출항일시 가져오기
	    LocalDateTime arrivalDate = voyage.getArrivalDate();
	    LocalDateTime exportDate = voyage.getDepartureDate();
	    
	    
	    // 대기 시간 + 작업 시간 계산
	    double avgWaitingTime = port.getAvgWaitingTime();
	    double avgWorkingTime = port.getAvgWorkingTime();
	    
	    long waitingHours = (long) avgWaitingTime; 
	    long waitingMinutes = (long) ((avgWaitingTime - waitingHours) * 60);
	    long workingHours = (long) avgWorkingTime;
	    long workingMinutes = (long) ((avgWorkingTime - workingHours) * 60);
	    
	    
	    // 출항 예정 일시 = 입항 예정 일시 + 대기 시간 + 작업 시간
	    exportDate = arrivalDate.plusHours(waitingHours + workingHours)
	                           .plusMinutes(waitingMinutes + workingMinutes);
	    
	    arrivalDate = arrivalDate.plusYears(2);
        exportDate = exportDate.plusYears(2);
	    
		
        // 계산 편의성을 위해 두 종류로 나누어 값 전달
        // 1. 출항 일시, 입항 일시 yyyy-MM-dd 형태 변환, 설정 (표시용)
        String arrivalDateDisplayStr = arrivalDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String exportDateDisplayStr = exportDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 2. 실제 계산을 위한 LocalDateTime 포맷 (서버 처리용 - hidden)
        String arrivalDateFullStr = arrivalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        String exportDateFullStr = exportDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
		
		
		// 대기 시간, 작업 시간 변환
		// 1. 작업 시간
		double time = port.getAvgWorkingTime();
		int hour = (int) time;
		int minute = (int) ((time - hour) * 60);
		
		// 2. 대기 시간
		int hour1 = (int) avgWaitingTime;
		int minute1 = (int) ((avgWaitingTime - hour1) * 60);
		
		
		
		// 기본 값 설정
		model.addAttribute("portName", port.getPortName());
		model.addAttribute("tonnage", ship.getTonnage());
		model.addAttribute("importDateDisplay", arrivalDateDisplayStr);
		model.addAttribute("exportDateDisplay", exportDateDisplayStr);
		model.addAttribute("importDateFull", arrivalDateFullStr);
		model.addAttribute("exportDateFull", exportDateFullStr);
		model.addAttribute("workingHour", hour);
	    model.addAttribute("workingMinute", minute);
	    model.addAttribute("waitingHour", hour1);
	    model.addAttribute("waitingMinute", minute1);
		model.addAttribute("shipName", ship.getShipName());
		model.addAttribute("callSign", callSign);
		
	    
	    // 저장 버튼 상태 플래그
	    model.addAttribute("isSaveEnabled", true);

		// 기존 세션 확인 및 값 전달
		if(session.getAttributeNames().hasMoreElements()) {
		   model.addAttribute("session_port",(String) session.getAttribute("session_port"));
		   model.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
		}
		
		return "pages/calculator";
	    }
}

	
