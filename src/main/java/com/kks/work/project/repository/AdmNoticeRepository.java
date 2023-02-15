package com.kks.work.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kks.work.project.vo.Notice;

@Mapper
public interface AdmNoticeRepository {

	// 공지 등록
	@Insert("""
				INSERT INTO notice
					SET regDate = NOW(),
					updateDate = NOW(),
					memberId = #{memberId},
					title = #{title},
					`body` = #{body}
				""")
	public void writeNotice(int memberId, String title, String body);
		
	// 공지 가져오기
	@Select("""
			SELECT *
			FROM notice
			WHERE id = #{id}
			""")
	public Notice getNotice(int id);
	
	
	// 공지글에 작성자 이름표시
	@Select("""
			SELECT N.*, M.name AS writerName 
				FROM notice AS N
				INNER JOIN `member` AS M
				ON N.MemberId = M.id
				WHERE N.id = #{id}
				ORDER BY N.id
			""")
	public Notice getForPrintNotice(int id);
	
	// 공지 목록
	@Select("""
			<script>
				SELECT N.*, M.name AS writerName
				FROM notice AS N
				INNER JOIN `member` AS M
				ON N.MemberId = M.id 
				WHERE 1 = 1
				<if test="searchKeyword != ''">
					AND title LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
				ORDER BY id DESC
				LIMIT #{limitStart}, #{itemsInAPage}
			</script>
			""")
	public List<Notice> getNotices(String searchKeywordTypeCode, String searchKeyword, int limitStart, int itemsInAPage);
	
	@Select("SELECT LAST_INSERT_ID()")
	public int getLastInsertId();

	// 공지 목록 개수
	@Select("""
			<script>
				SELECT COUNT(*)
				FROM notice
				WHERE 1 = 1
				<if test="searchKeyword != ''">
					AND storeName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
			</script>
			""")
	public int getNoticesCount(String searchKeywordTypeCode, String searchKeyword);

	// 작성된 공지 삭제
	@Delete("""
			DELETE FROM notice
			WHERE id = #{id}
		""")
	public void deleteNotice(int id);

	// 공지 수정
	@Update("""
			<script>
				UPDATE notice
				<set>
					updateDate = NOW(),
					<if test="title != null">
						title = #{title},
					</if>
					<if test="body != null">
						body = #{body}
					</if>
				</set>
				WHERE id = #{id}
			</script>		
			""")
	public void modifyNotice(int id, String title, String body);

	// 조회 카운트
	@Select("""
			<script>
				UPDATE notice
					SET viewCount = viewCount + 1
					WHERE id = #{id}
			</script>
			""")
	public int increaseViewCount(int id);

	// 조회 수 가져오기
	@Select("""
			<script>
				SELECT viewCount 
					FROM notice
					WHERE id = #{id}
			</script>
		""")
	public Integer getNoticeViewCount(int id);

}
