package kr.co.crud.service;

import kr.co.crud.dao.BoardDAO;
import kr.co.crud.domain.BoardVO;
import kr.co.crud.domain.CommentVO;
import kr.co.crud.domain.FileVO;
import kr.co.crud.utils.PageHandler;
import kr.co.crud.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BoardService {

    @Autowired
    private BoardDAO dao;

    /* Board List */
    public List<BoardVO> selectBoardList(Model model, SearchCondition sc){

        int totalCnt = dao.countBoard(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<BoardVO> list = dao.selectBoardList(sc);

        List<Integer> noList = new ArrayList<>();
        int startNo = totalCnt - (sc.getPage() - 1) * sc.getPageSize();

        for (int i = 0; i < list.size(); i++) {
            noList.add(startNo - i);
        }

        model.addAttribute("list", list);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("noList", noList);

        return list;
    }

    /* Board Write */
    public void insertBoard(BoardVO boardVO){

        // 게시글 NO (today) 생성
        String today = getTodayDateTime();
        boardVO.setNo(today);

        // 게시글 작성
        dao.insertBoard(boardVO);

        // 새 파일명 생성
        log.warn("name: " + boardVO.getName());
        if(boardVO.getName() != null){
            String oriName = boardVO.getName();
            String tempName = oriName.substring(oriName.lastIndexOf("."));
            String newName = UUID.randomUUID().toString() + tempName;

            // 파일 정보 작성
            FileVO fileVO = new FileVO();
            fileVO.setParent(today);
            fileVO.setOriName(oriName);
            fileVO.setNewName(newName);

            dao.insertFile(fileVO);
        }
    }

    /* today String */
    public static String getTodayDateTime(){
        // 오늘 날짜 가져오기
        LocalDateTime now = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.now());

        // 형식에 맞게 문자열로 변환하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTimeString = now.format(formatter);

        return dateTimeString;
    }

     /* Board View */
    public BoardVO selectBoard(BoardVO boardVO){
        return dao.selectBoard(boardVO);
    }

    /* Board File Download */
    public ResponseEntity<Resource> fileDownload(String oriName) throws IOException {

        String uploadPath = "/Users/yiwonjeong/Desktop/upload/";
        Path path = Paths.get(uploadPath, oriName);

        String contentType = Files.probeContentType(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(oriName)
                .build());

        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        Resource resource = new InputStreamResource(Files.newInputStream(path));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }

    /* File Select */
    public FileVO selectFile(int fno){
        return dao.selectFile(fno);
    }

    /* Board HIT */
    public void updateBoardHit(BoardVO boardVO){
        dao.updateBoardHit(boardVO);
    }

    /* Board Modify */
    public void updateBoard(BoardVO boardVO){

         dao.updateBoard(boardVO);

        if(boardVO.getName() != null){
            // 새 파일명 생성
            String oriName = boardVO.getName();
            String tempName = oriName.substring(oriName.lastIndexOf("."));
            String newName = UUID.randomUUID().toString() + tempName;
            int fno = boardVO.getFno();

              // 파일 정보 작성
            FileVO fileVO = new FileVO();
            fileVO.setOriName(oriName);
            fileVO.setNewName(newName);
            fileVO.setFno(fno);


            dao.updateFile(fileVO);
        }

    }

    /* Board Delete */
    public void deleteBoard(BoardVO boardVO){
        dao.deleteBoard(boardVO);
    }

    /* Comment Write */
    public void insertComment(CommentVO commentVO) {
        dao.insertComment(commentVO);
    }

    /* Comment List */
    public List<CommentVO> selectComment(CommentVO commentVO) {
        return dao.selectComment(commentVO);
    }

    /* Comment Update */
    public void updateComment(CommentVO commentVO) {
        dao.updateComment(commentVO);
    }

    /* Comment Delete */
    public void deleteComment(CommentVO commentVO) {
        dao.deleteComment(commentVO);
    }

}
