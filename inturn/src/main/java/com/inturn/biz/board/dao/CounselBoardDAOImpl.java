package com.inturn.biz.board.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("CounselBoardDAO")
public class CounselBoardDAOImpl implements CounselBoardDAO{
	@Autowired
	SqlSessionTemplate mybatis;
}
