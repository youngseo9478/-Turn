package com.inturn.biz.board.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inturn.biz.board.dao.ReplyDAO;
import com.inturn.biz.board.vo.ReplyVO;

@Service("ReplyService")
public class ReplyServiceImpl implements ReplyService {
	@Resource(name = "ReplyDAO")
	ReplyDAO dao;
	
	/**
	 * 내 게시글에 대한 가장 최근 댓글 10개
	 */
	@Override
	public List<ReplyVO> freeBoardAlarm(String id){
		return dao.freeBoardAlarm(id);
	}
	
	/**
	 * 댓글 입력 함수
	 */
	@Override
	public int insertFBReply(ReplyVO vo) {
		java.util.Date udate = new java.util.Date();
		Date regDate = new Date(udate.getTime());
		vo.setRegDate(regDate);
		return dao.insertFBReply(vo);
	}

	/**
	 * 대댓글 입력함수
	 * 대댓글 입력시 해당 부모위치를 자신의 위치로하고
	 * 해당 부모위치 부터 그 위로 모두 position을 1개씩 증가시켜준다.
	 * 이렇게 하면 position으로 댓글을 불러왔을 때,
	 * 대댓글까지 모두 그 위치가 알맞게 맞춰져 정렬된다.
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertFBReReply(ReplyVO vo) {
		int row = 0;
		row += dao.upPosition(dao.getPosition(vo.getParentNum()));
		row += dao.insertFBReReply(vo);
		return row;
	}

	/**
	 * 댓글을 불러오는 함수로
	 * 페이징는 처리 게시판과 똑같다.
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public HashMap<String, Object> getFBReplies(int page_num, int fb_num) {
		// 전체 게시판 개수를 가져옴
		int total_boards = dao.countReplies(fb_num);
		// 게시판을 10개씩 페이징 처리했을 때, 총 몇개의 목록이 나오는지 계산
		int count_page = (total_boards + 9) / 10;
		// 만약 마지막 페이지의 게시글이 1개 인데 삭제된 경우
		if (count_page < page_num)
			page_num--;
		// 마지막 페이지의 개수를 계산
		int reminder = total_boards % 10;
		if (reminder == 0)
			reminder = 10;
		// 클릭한 해당 페이지의 처음 번호와, 마지막번호를 계산
		int limit = (count_page - page_num) * 10 + reminder; // 마지막번호
		int offset = (count_page - (page_num + 1)) * 10 + reminder; // 첫 번호
		// 결과를 계산, 반환하기 위한 Map 정의
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> result = new HashMap<String, Object>();

		if (offset < 0)
			offset = 0; // 마지막 페이지일 경우 음수 값이 되므로 첫 번호를 0으로 만들어줌
		result.put("limit", limit + 1); // 미리 게시판 번호의 수를 넣어놓는다.
		limit -= offset; // 마지막과 첫 번호의 차를 구해 가져올 개수를 구함
		map.put("offset", offset); // 계시판 페이징의 시작 게시판 번호 값
		map.put("limit", limit); // 시작번호로 부터 몇개를 가져올지 계산한 값을 넣는다.
		map.put("fb_num", fb_num); // 시작번호로 부터 몇개를 가져올지 계산한 값을 넣는다.
		// 결과값을 list에 넣는다.
		List<ReplyVO> list = dao.getFBReplies(map);
		// 총 페이지수가 몇개인지도 보내줘야 하므로 Map구조로 put해서 result를 return
		result.put("list", list);
		result.put("count_page", count_page);
		result.put("thisPage", page_num);
		return result;
	}

	@Override
	public int modifyReply(ReplyVO vo) {
		return dao.modifyReply(vo);
	}

	@Override
	public int upPosition(int position) {
		return dao.upPosition(position);
	}

	@Override
	public int downPosition(int position) {
		return dao.downPosition(position);
	}

	/**
	 * 대댓글 때문에 position의 위치가 매우 중요하다.
	 * position으로 정렬하여 나타내기 때문이다.
	 * 따라서 해당부분 댓글이 삭제되면 그 위의 모든 댓글의
	 * position 값을 -1씩 해주어야 한다.
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteReply(int rp_num) {
		int row = 0;
		ReplyVO vo = dao.getReply(rp_num);
		row += dao.downPosition(vo.getPosition());
		row += dao.deleteReply(rp_num);
		row += dao.deleteChildReply(rp_num);
		return row;
	}

	@Override
	public List<ReplyVO> mentorBoardAlarm(String id) {
		return dao.mentorBoardAlarm(id);
	}

	@Override
	public int insertMBReply(ReplyVO vo) {
		return dao.insertMBReply(vo);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertMBReReply(ReplyVO vo) {
		int row = 0;
		row += dao.upPosition(dao.getPosition(vo.getParentNum()));
		row += dao.insertMBReReply(vo);
		return row;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public HashMap<String, Object> getMBReplies(int page_num, int mb_num) {
		// 전체 게시판 개수를 가져옴
		int total_boards = dao.countMBReplies(mb_num);
		// 게시판을 10개씩 페이징 처리했을 때, 총 몇개의 목록이 나오는지 계산
		int count_page = (total_boards + 9) / 10;
		// 만약 마지막 페이지의 게시글이 1개 인데 삭제된 경우
		if (count_page < page_num)
			page_num--;
		// 마지막 페이지의 개수를 계산
		int reminder = total_boards % 10;
		if (reminder == 0)
			reminder = 10;
		// 클릭한 해당 페이지의 처음 번호와, 마지막번호를 계산
		int limit = (count_page - page_num) * 10 + reminder; // 마지막번호
		int offset = (count_page - (page_num + 1)) * 10 + reminder; // 첫 번호
		// 결과를 계산, 반환하기 위한 Map 정의
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> result = new HashMap<String, Object>();

		if (offset < 0)
			offset = 0; // 마지막 페이지일 경우 음수 값이 되므로 첫 번호를 0으로 만들어줌
		result.put("limit", limit + 1); // 미리 게시판 번호의 수를 넣어놓는다.
		limit -= offset; // 마지막과 첫 번호의 차를 구해 가져올 개수를 구함
		map.put("offset", offset); // 계시판 페이징의 시작 게시판 번호 값
		map.put("limit", limit); // 시작번호로 부터 몇개를 가져올지 계산한 값을 넣는다.
		map.put("mb_num", mb_num); // 시작번호로 부터 몇개를 가져올지 계산한 값을 넣는다.
		// 결과값을 list에 넣는다.
		List<ReplyVO> list = dao.getMBReplies(map);
		// 총 페이지수가 몇개인지도 보내줘야 하므로 Map구조로 put해서 result를 return
		result.put("list", list);
		result.put("count_page", count_page);
		result.put("thisPage", page_num);
		return result;
	}
	
	
//	멘티 게시물 댓글 관련///////////////////////////////////////

	@Override
	public int insertTBReply(ReplyVO vo) {
		java.util.Date udate = new java.util.Date();
		Date regDate = new Date(udate.getTime());
		vo.setRegDate(regDate);
		return dao.insertTBReply(vo);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertTBReReply(ReplyVO vo) {
		int row = 0;
		row += dao.upPosition(dao.getPosition(vo.getParentNum()));
		row += dao.insertTBReReply(vo);
		return row;
	}

	@Override
	public List<ReplyVO> menteeBoardAlarm(String id) {
		return dao.menteeBoardAlarm(id);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public HashMap<String, Object> getTBReplies(int page_num, int tb_num) {
		// 전체 댓글 개수를 가져옴
				int total_boards = dao.countTBReplies(tb_num);
				System.out.println(total_boards);
				// 댓글을 10개씩 페이징 처리했을 때, 총 몇개의 목록이 나오는지 계산
				int count_page = (total_boards + 9) / 10;
				// 만약 마지막 페이지의 댓글이 1개 인데 삭제된 경우
				if (count_page < page_num)
					page_num--;
				// 마지막 페이지의 댓글 개수를 계산
				int reminder = total_boards % 10;
				if (reminder == 0)
					reminder = 10;
				// 클릭한 해당 페이지의 처음 번호와, 마지막번호를 계산
				int limit = (count_page - page_num) * 10 + reminder; // 마지막번호
				int offset = (count_page - (page_num + 1)) * 10 + reminder; // 첫 번호
				// 결과를 계산, 반환하기 위한 Map 정의
				HashMap<String, Object> map = new HashMap<String, Object>();
				HashMap<String, Object> result = new HashMap<String, Object>();

				if (offset < 0)
					offset = 0; // 마지막 페이지일 경우 음수 값이 되므로 첫 번호를 0으로 만들어줌
				result.put("limit", limit + 1); // 미리 게시판 번호의 수를 넣어놓는다.
				limit -= offset; // 마지막과 첫 번호의 차를 구해 가져올 개수를 구함
				map.put("offset", offset); // 계시판 페이징의 시작 게시판 번호 값
				map.put("limit", limit); // 시작번호로 부터 몇개를 가져올지 계산한 값을 넣는다.
				map.put("tb_num", tb_num); // 시작번호로 부터 몇개를 가져올지 계산한 값을 넣는다.
				// 결과값을 list에 넣는다.
				List<ReplyVO> list = dao.getTBReplies(map);
				// 총 페이지수가 몇개인지도 보내줘야 하므로 Map구조로 put해서 result를 return
				result.put("list", list);
				result.put("count_page", count_page);
				result.put("thisPage", page_num);
				return result;
	}
}
