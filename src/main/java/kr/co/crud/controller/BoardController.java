package kr.co.crud.controller;

import kr.co.crud.domain.BoardVO;
import kr.co.crud.domain.CommentVO;
import kr.co.crud.domain.FileVO;
import kr.co.crud.security.MyUserDetails;
import kr.co.crud.service.BoardService;
import kr.co.crud.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class BoardController {

    @Autowired
    private BoardService service;

    /* Board List */
    @GetMapping("/")
    public String selectBoardList(Model model,
                                  @RequestParam Map map,
                                  @ModelAttribute SearchCondition sc,
                                  @AuthenticationPrincipal UserDetails details,
                                  Principal principal,
                                  HttpSession session) {

        sc.setMap(map);
        service.selectBoardList(model, sc);

        if(details != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String nickname = userDetails.getNickname();

            model.addAttribute("nickname", nickname);
            model.addAttribute("username", details.getUsername());
        }


        return "/board/list";
    }

    /* Board Write */
    @GetMapping("/board/write")
    public String write(Model model, @AuthenticationPrincipal UserDetails details){

        if(details != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String nickname = userDetails.getNickname();

            model.addAttribute("nickname", nickname);
            model.addAttribute("username", details.getUsername());
        }

        return "/board/write";
    }

    @PostMapping("/board/write")
    public ResponseEntity<BoardVO> insertBoard(@RequestBody BoardVO boardVO,
                                               @AuthenticationPrincipal UserDetails details){

        if(details != null) {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           boardVO.setUid(authentication.getName());    // 사용자 아이디
       }

        service.insertBoard(boardVO);

        return ResponseEntity.ok().body(boardVO);
    }

    /* File Upload */
    @PostMapping("/board/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){

        String uploadPath = "/Users/yiwonjeong/Desktop/upload";
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();
        //File uploadFile = new File(uploadPath, )

        log.warn("multipart upload: "+ file);
        log.warn("Original Filename: " + file.getOriginalFilename());
        log.warn("Content Type: " + file.getContentType());
        log.warn("Size: " + file.getSize());

        Path filePath = Paths.get(uploadPath, originalFilename);
         try {
        Files.write(filePath, file.getBytes());
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            log.error("Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file. Please try again.");
        }
    }

    /* Board View */
    @GetMapping("/board/view")
    public String selectBoard(BoardVO boardVO,
                              CommentVO commentVO,
                              @AuthenticationPrincipal UserDetails details,
                              @RequestParam("no") String no,
                              Model model){

        boardVO.setNo(no);
        commentVO.setNo(no);

        // 글 조회수 +1
        service.updateBoardHit(boardVO);

        // 글 가져오기
        BoardVO board = service.selectBoard(boardVO);

        model.addAttribute("board", board);
        model.addAttribute("no", no);

        // 댓글 가져오기
        List<CommentVO> comments = service.selectComment(commentVO);

        model.addAttribute("comments", comments);
        model.addAttribute("commentsSize", comments.size());

        if(details != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String nickname = userDetails.getNickname();

            model.addAttribute("nickname", nickname);
            model.addAttribute("username", details.getUsername());
        }

        return "/board/view";
    }

    /* Board File Download */
    @GetMapping("/board/fileDownload")
    @ResponseBody
    public ResponseEntity<Resource> fileDownload(@RequestParam("fno") String fno) throws IOException {

        int fnoInt = Integer.parseInt(fno);

        FileVO file = service.selectFile(fnoInt);

        ResponseEntity<Resource> response = service.fileDownload(file.getOriName());

        return response;

    }

    /* Board Modify */
    @GetMapping("/board/modify")
    public String modifyBoard(BoardVO boardVO,
                              @RequestParam("no") String no,
                              @AuthenticationPrincipal UserDetails details,
                              Model model){
        // 글 가져오기
        BoardVO board = service.selectBoard(boardVO);

        model.addAttribute("board", board);
        model.addAttribute("no", no);

        if(details != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String nickname = userDetails.getNickname();

            model.addAttribute("nickname", nickname);
            model.addAttribute("username", details.getUsername());
        }

        return "/board/modify";
    }

    @PostMapping("/board/modify")
    public ResponseEntity<BoardVO> updateBoard(@RequestBody BoardVO boardVO){

        log.warn("boardVO update: " + boardVO);

        service.updateBoard(boardVO);
        return ResponseEntity.ok().body(boardVO);
    }

    /* Board Delete */
    @PostMapping("/board/delete")
    public ResponseEntity<BoardVO> deleteBoard(@RequestBody BoardVO boardVO){
        service.deleteBoard(boardVO);
        return ResponseEntity.ok().body(boardVO);
    }

    /* Comment Write */
    @PostMapping("/comment/write")
    public ResponseEntity<CommentVO> insertComment(@RequestBody CommentVO commentVO,
                                                   @AuthenticationPrincipal UserDetails details){

        if(details != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            commentVO.setUid(authentication.getName());
        }

        service.insertComment(commentVO);
        return ResponseEntity.ok().body(commentVO);
    }

    /* Comment List */
    @GetMapping("/comment/list")
    public String selectComment(CommentVO commentVO, Model model){

        List<CommentVO> comments = service.selectComment(commentVO);
        model.addAttribute("comments", comments);

        return "/board/view";
    }

    /* Comment Update */
    @PostMapping("/comment/update")
    public ResponseEntity<CommentVO> updateComment(@RequestBody CommentVO commentVO){
        service.updateComment(commentVO);
        return ResponseEntity.ok().body(commentVO);
    }

    /* Comment Delete */
    @PostMapping("/comment/delete")
    public ResponseEntity<CommentVO> deleteComment(@RequestBody CommentVO commentVO){
        service.deleteComment(commentVO);
        return ResponseEntity.ok().body(commentVO);
    }




}
