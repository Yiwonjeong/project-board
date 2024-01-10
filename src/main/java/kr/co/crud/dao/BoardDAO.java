package kr.co.crud.dao;

import kr.co.crud.domain.BoardVO;
import kr.co.crud.domain.CommentVO;
import kr.co.crud.domain.FileVO;
import kr.co.crud.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardDAO {

    /* Board List */
    public List<BoardVO> selectBoardList(SearchCondition sc);

    /* Board Count */
    public int countBoard(SearchCondition sc);

    /* Board Write */
    public void insertBoard(BoardVO boardVO);

    /* File Write  */
    public void insertFile(FileVO fileVO);

    /* File Select */
    public FileVO selectFile(int fno);

    /* File Modify */
    public void updateFile(FileVO fileVO);

    /* Board View */
    public BoardVO selectBoard(BoardVO boardVO);

    /* Board HIT */
    public void updateBoardHit(BoardVO boardVO);

    /* Board Modify */
    public void updateBoard(BoardVO boardVO);

    /* Board Delete */
    public void deleteBoard(BoardVO boardVO);

    /* Comment Write */
    public void insertComment(CommentVO commentVO);

    /* Comment List */
    public List<CommentVO> selectComment(CommentVO commentVO);

    /* Comment Update */
    public void updateComment(CommentVO commentVO);

    /* Comment Delete */
    public void deleteComment(CommentVO commentVO);

}
