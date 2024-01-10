package kr.co.crud.dao;

import kr.co.crud.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDAO {

    /* User Insert  */
    public void insertUser(UserVO userVO);

    /* 이미 회원가입한 사용자인지 확인 */
    public int selectUserDup(String uid);
}
