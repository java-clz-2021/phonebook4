package com.javaex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaex.dao.PhoneDao;
import com.javaex.vo.PersonVo;

@Controller
public class PhoneController {

	// 필드
	@Autowired
	private PhoneDao phoneDao;
	

	
	// 생성자
	// 메소드-gs
	// 메소드-일반

	// 리스트
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(Model model) {
		System.out.println("[PhoneController.list]");

		// Dao 사용  --> @Autowired
		//PhoneDao phoneDao = new PhoneDao();    

		// Dao의 메소드로 데이터 가져오기
		List<PersonVo> personList = phoneDao.getPersonList();
		System.out.println(personList);

		// model담기 (택배박스에 담기) --> DispatcherServlet전달된다 ---> request의 attritube영역에 넣는다
		model.addAttribute("personList", personList);

		// view
		return "/WEB-INF/views/list.jsp"; // DispatcherServlet 야 /WEB-INF/views/list.jsp 에 포워드해줘!!
	}

	// 쓰기폼
	@RequestMapping(value = "/writeForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String writeForm() {
		System.out.println("[PhoneController.writeForm]");

		return "/WEB-INF/views/writeForm.jsp"; // DispatcherServlet 야 /WEB-INF/views/writeForm.jsp 에 포워드해줘!!
	}

	// http://localhost:8088/phonebook4/writeForm?name=정우성&hp=010-2222-2222&company=02-2222-2222

	// 쓰기
	
	/*
	//파라미터를 1개씩 받을때 
	@RequestMapping(value = "/write", method = {RequestMethod.GET, RequestMethod.POST})
	public String write(@RequestParam("name") String name,
						@RequestParam("hp") String hp,
						@RequestParam("company") String company) {
		System.out.println("[PhoneController.write]");
		
		PersonVo personVo = new PersonVo(name, hp, company);
		System.out.println(personVo);
	 
		return ""; 
	}
	*/

	// 쓰기
	// @ModelAttribute 일때
	@RequestMapping(value = "/write", method = { RequestMethod.GET, RequestMethod.POST })
	public String write(@ModelAttribute PersonVo personVo) {
		System.out.println("[PhoneController.write]");

		// @ModelAttribute --> new PersonVo()
		// --> 기본생성자 + setter
		System.out.println(personVo);

		// Dao 사용  --> @Autowired
		//PhoneDao phoneDao = new PhoneDao();

		// Dao 의 personInser() 이용해서 데이터 저장
		int count = phoneDao.personInsert(personVo);

		// view --> 리다이렉트
		return "redirect:/list";
	}

	
	//쓰기 //파라미터가 있을때도 있고 없을때도 있고
	/*
	@RequestMapping(value = "/write", method = {RequestMethod.GET, RequestMethod.POST}) 
	public String write(@RequestParam("name") String name,
						@RequestParam("hp") String hp,
						@RequestParam(value="company", required=false, defaultValue="-1") String company) { 
		System.out.println("[PhoneController.write]");
	
		PersonVo personVo = new PersonVo(name, hp, company);
		System.out.println(personVo);
	
		return ""; 
	}
	*/

	// 삭제
	@RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
	public String delete(@RequestParam("no") int personId) {
		System.out.println("[PhoneController.delete]");
		System.out.println(personId);

		// Dao 사용  --> @Autowired
		//PhoneDao phoneDao = new PhoneDao();

		// Dao 의 personDelete() 이용해서 데이터 삭제
		int count = phoneDao.personDelete(personId);

		// view --> 리다이렉트
		return "redirect:/list";
	}

	// 수정폼
	@RequestMapping(value = "/updateForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateForm(Model model, @RequestParam("no") int personId) {
		System.out.println("[PhoneController.updateForm]");
		System.out.println(personId);

		// Dao 사용  --> @Autowired
		//PhoneDao phoneDao = new PhoneDao();

		// Dao 의 personDelete() 이용해서 데이터 삭제
		PersonVo personVo = phoneDao.getPerson(personId);

		// model담기 (택배박스에 담기) --> DispatcherServlet전달된다 ---> request의 attritube영역에 넣는다
		model.addAttribute("personVo", personVo);

		// view --> jsp파일
		return "/WEB-INF/views/updateForm.jsp"; // DispatcherServlet 야 /WEB-INF/views/writeForm.jsp 에 포워드해줘!!

	}

	// 수정
	// @ModelAttribute 일때
	@RequestMapping(value = "/update", method = { RequestMethod.GET, RequestMethod.POST })
	public String update(@ModelAttribute PersonVo personVo) {
		System.out.println("[PhoneController.update]");

		// @ModelAttribute --> new PersonVo()
		// --> 기본생성자 + setter
		System.out.println(personVo);

		// Dao 사용
		PhoneDao phoneDao = new PhoneDao();

		// Dao 의 personInser() 이용해서 데이터 저장
		int count = phoneDao.personUpdate(personVo);

		// view --> 리다이렉트
		return "redirect:/list";
	}

	
	
	
	
	/*********************************************************************************/
	// PathVariable 테스트
	@RequestMapping(value = "/board/read/{no}", method = { RequestMethod.GET, RequestMethod.POST })
	public String read(@PathVariable("no") int boardNo) {
		System.out.println("PathVariable [read]");

		// localhost:8088/phonebook4/board/read/1
		// localhost:8088/phonebook4/board/read/2
		// localhost:8088/phonebook4/board/read/100

		System.out.println(boardNo);

		return "";
	}

	@RequestMapping(value = "/test")
	public String test() {
		System.out.println("test");

		return "/WEB-INF/views/test.jsp"; // DispatcherServlet 야 /WEB-INF/views/test.jsp 에 포워드해줘!!
	}

}
