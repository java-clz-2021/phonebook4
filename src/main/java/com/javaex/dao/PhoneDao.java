package com.javaex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.PersonVo;



@Repository
public class PhoneDao {

	@Autowired
	private DataSource dataSource;
	
	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	/*
	//applicationContext.xml -- 세팅
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "phonedb";
	private String pw = "phonedb";
	*/

	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			//Class.forName(driver);

			// 2. Connection 얻어오기
			//conn = DriverManager.getConnection(url, id, pw);
			conn = dataSource.getConnection();
			
			// System.out.println("접속성공");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 사람 추가
	public int personInsert(PersonVo personVo) {
		int count = -1;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " INSERT INTO person ";
			query += " VALUES (seq_person_id.nextval, ?, ?, ?) ";
			// System.out.println(query);

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, personVo.getName()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, personVo.getHp()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, personVo.getCompany()); // ?(물음표) 중 3번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			System.out.println("[" + count + "건 추가되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		return count;
	}

	// 사람 리스트(검색안할때)
	public List<PersonVo> getPersonList() {
		return getPersonList("");
	}

	// 사람 리스트(검색할때)
	public List<PersonVo> getPersonList(String keword) {
		List<PersonVo> personList = new ArrayList<PersonVo>();

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " select  person_id, ";
			query += "         name, ";
			query += "         hp, ";
			query += "         company ";
			query += " from person";

			if (keword != "" || keword == null) { //검색어 있을때
				query += " where name like ? ";
				query += " or hp like  ? ";
				query += " or company like ? ";
				query += " order by person_id desc ";
				
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기

				pstmt.setString(1, '%' + keword + '%'); // ?(물음표) 중 1번째, 순서중요
				pstmt.setString(2, '%' + keword + '%'); // ?(물음표) 중 2번째, 순서중요
				pstmt.setString(3, '%' + keword + '%'); // ?(물음표) 중 3번째, 순서중요
			
			} else {                             //검색어 없을때
				query += " order by person_id desc ";
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			}

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				PersonVo personVo = new PersonVo(personId, name, hp, company);
				personList.add(personVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return personList;

	}

	// 사람 수정
	public int personUpdate(PersonVo personVo) {
		int count = -1;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " update person ";
			query += " set name = ? , ";
			query += "     hp = ? , ";
			query += "     company = ? ";
			query += " where person_id = ? ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, personVo.getName()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, personVo.getHp()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, personVo.getCompany()); // ?(물음표) 중 3번째, 순서중요
			pstmt.setInt(4, personVo.getPersonId()); // ?(물음표) 중 4번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			System.out.println(count + "건 수정되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

	// 사람 삭제
	public int personDelete(int personId) {
		int count = -1;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " delete from person ";
			query += " where person_id = ? ";
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setInt(1, personId);// ?(물음표) 중 1번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			System.out.println(count + "건 삭제되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

	// 사람 1명 가져오기
	public PersonVo getPerson(int personId) {
		PersonVo personVo = null;
		
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select  person_id, ";
			query += "         name, ";
			query += "         hp, ";
			query += "         company ";
			query += " from person";
			query += " where person_id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, personId);
			
			rs = pstmt.executeQuery();
		
			// 4.결과처리
			while(rs.next()) {
				int pid = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
				
				personVo = new PersonVo(pid, name, hp, company);
			}
			
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return personVo;
	}

}
