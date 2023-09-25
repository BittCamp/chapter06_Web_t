package user.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import user.bean.UserImageDTO;
import user.service.UserServiceUpload;

@Controller
@RequestMapping(value="user")
public class UserControllerUploadAJax {
	@Autowired
	private UserServiceUpload userServiceUpload;
	
	@GetMapping(value="uploadForm_AJax")
	public String uploadForm_AJax() {
		return "/user/uploadForm_AJax";
	}
	
	//----------- 한 번에 여러개의 파일을 선택 -----------------------
	@PostMapping(value="upload_AJax", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String upload(@ModelAttribute UserImageDTO userImageDTO,
			             @RequestParam("img[]") List<MultipartFile> list,
			             HttpSession session) {
		
		//실제폴더
		String filePath = session.getServletContext().getRealPath("/WEB-INF/storage");
		System.out.println("실제폴더 = " + filePath);
		
		String fileName;
		File file;
		String result = "";
		
		//파일명만 모아서 DB로 보내기
		List<String> fileNameList = new ArrayList<String>();
		
		for(MultipartFile img : list) {
			fileName = img.getOriginalFilename();
			file = new File(filePath, fileName);
			
			fileNameList.add(fileName);
			
			try {
				img.transferTo(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			result += "<span><img src='/chapter06_Web/storage/" + fileName + "' width='300' height='300' /></span>";
			
		}//for
		
		//DB
		userServiceUpload.upload(userImageDTO, fileNameList);
		
		return result;
	}
}
