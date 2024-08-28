package Insuleng.Insuleng_Backend.src.community.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.community.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CommunityRepository {
    private final JdbcTemplate jdbcTemplate;
    public CommunityRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createPost(Long userId, PostDto postDto, String imgUrl){
        String sql = "INSERT INTO post(count_comment, count_like, count_parent_comment, user_id, contents, img_url, topic, status,count_scrap)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"post_id"});
            ps.setInt(1, 0);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setLong(4, userId);
            ps.setString(5, postDto.getContents());
            ps.setString(6, imgUrl);
            ps.setString(7, postDto.getTopic());
            ps.setString(8, "ACTIVE");
            ps.setInt(9, 0);
            return ps;
        }, keyHolder);
    }
    public boolean testUserId(Long userId){

        int rowCount = this.jdbcTemplate.queryForObject("select count(*) from user where user_id = ? and status = 'ACTIVE'", Integer.class, userId);
        if(rowCount == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isPostByIdAndStatusActive(Long postId) {
        String sql = "SELECT COUNT(*) FROM post WHERE post_id = ? AND status = 'ACTIVE'";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{postId}, Integer.class);
        return count != null && count > 0;
    }

    public void deletePost(Long userId, Long postId) {
        String sql = "UPDATE post SET status = 'INACTIVE' WHERE post_id = ? AND user_id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, postId, userId);
    }
    public boolean isUserOwnerOfPost(Long userId, Long postId) {
        String sql = "SELECT COUNT(*) FROM post WHERE post_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{postId, userId}, Integer.class);
        return count != null && count > 0;
    }

    public void updatePost(Long userId, UpdatePostDto updatePostDto, String imgUrl){
        String sql =  "UPDATE post SET topic = ?, contents = ?, img_url = ? WHERE post_id = ? AND user_id = ?";

        jdbcTemplate.update(sql,
                updatePostDto.getTopic(),
                updatePostDto.getContents(),
                imgUrl,
                updatePostDto.getPostId(),
                userId);

    }
    public List<PostSummaryDto> searchPosts(Long userId, String keyword) {
        String sql = "SELECT p.post_id, p.topic, p.contents, p.count_like, p.count_comment, p.count_scrap, p.img_url, u.nickname " +
                "FROM post p " +
                "JOIN user u ON p.user_id = u.user_id " +
                "WHERE (p.topic LIKE ? OR p.contents LIKE ?) AND p.status = 'ACTIVE'";


        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%", "%" + keyword + "%"}, (rs, rowNum) -> {
            Long postId = rs.getLong("post_id");
            String topic = rs.getString("topic");
            String contents = rs.getString("contents");
            int countLike = rs.getInt("count_like");
            int countComment = rs.getInt("count_comment");
            int countScrap = rs.getInt("count_scrap");
            String imgUrl = rs.getString("img_url");
            String authorName = rs.getString("nickname");

            String contentsSnippet = getSnippet(contents);

            return new PostSummaryDto(postId, topic, contentsSnippet, countLike, countComment, imgUrl, authorName, countScrap);
        });
    }
    private String getSnippet(String content) {
        int snippetLength = 100; // 내용의 일부만 표시, 예: 100자
        return content.length() > snippetLength ? content.substring(0, snippetLength) + "..." : content;
    }

    public boolean checkPostLike(Long userId, Long postId) {
        String sql = "SELECT COUNT(*) FROM post_like WHERE user_id = ? AND post_id = ? AND status = 'ACTIVE'";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, postId}, Integer.class);
        return count != null && count > 0;
    }
    public void addPostLike(Long userId, Long postId) {
        String sql = "INSERT INTO post_like (user_id, post_id, status) VALUES (?, ?, 'ACTIVE')";
        jdbcTemplate.update(sql, userId, postId);
    }

    public boolean checkPostLikeInactive(Long userId, Long postId) {
        String sql = "SELECT COUNT(*) FROM post_like WHERE user_id = ? AND post_id = ? AND status = 'INACTIVE'";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, postId}, Integer.class);
        return count != null && count > 0;
    }

    public void updatePostLikeStatus(Long userId, Long postId, Status status) {
        String sql = "UPDATE post_like SET status = ? WHERE user_id = ? AND post_id = ?";
        jdbcTemplate.update(sql, status.toString(), userId, postId);
    }
    public void increasePostLikeCount(Long postId) {
        String sql = "UPDATE post SET count_like = count_like + 1 WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }
    public void decreasePostLikeCount(Long postId) {
        String sql = "UPDATE post SET count_like = count_like - 1 WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }


    public boolean isCommentByIdAndStatusActive(Long commentId) {
        String sql = "SELECT COUNT(*) FROM comment WHERE comment_id = ? AND status = 'ACTIVE'";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{commentId}, Integer.class);
        return count != null && count > 0;
    }
    public boolean checkCommentLike(Long userId, Long commentId) {
        String sql = "SELECT COUNT(*) FROM comment_like WHERE user_id = ? AND comment_id = ? AND status = 'ACTIVE'";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, commentId}, Integer.class);
        return count != null && count > 0;
    }
    public boolean checkCommentLikeInactive(Long userId, Long commentId) {
        String sql = "SELECT COUNT(*) FROM comment_like WHERE user_id = ? AND comment_id = ? AND status = 'INACTIVE'";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, commentId}, Integer.class);
        return count != null && count > 0;
    }
    public void updateCommentLikeStatus(Long userId, Long commentId, Status status) {
        String sql = "UPDATE comment_like SET status = ? WHERE user_id = ? AND comment_id = ?";
        jdbcTemplate.update(sql, status.toString(), userId, commentId);
    }
    public void addCommentLike(Long userId, Long commentId) {
        String sql = "INSERT INTO comment_like (user_id, comment_id, status) VALUES (?, ?, 'ACTIVE')";
        jdbcTemplate.update(sql, userId, commentId);
    }
    public void increaseCommentLikeCount(Long commentId) {
        String sql = "UPDATE comment SET count_like = count_like + 1 WHERE comment_id = ?";
        jdbcTemplate.update(sql, commentId);
    }
    public void decreaseCommentLikeCount(Long commentId) {
        String sql = "UPDATE comment SET count_like = count_like - 1 WHERE comment_id = ?";
        jdbcTemplate.update(sql, commentId);
    }

    public boolean checkPostScrap(Long userId, Long postId) {
        String sql = "SELECT COUNT(*) FROM scrap WHERE user_id = ? AND post_id = ? AND status = 'ACTIVE'";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, postId}, Integer.class);
        return count != null && count > 0;
    }
    public boolean checkPostScrapInactive(Long userId, Long postId) {
        String sql = "SELECT COUNT(*) FROM scrap WHERE user_id = ? AND post_id = ? AND status = 'INACTIVE'";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, postId}, Integer.class);
        return count != null && count > 0;
    }
    public void updatePostScrapStatus(Long userId, Long postId, Status status) {
        String sql = "UPDATE scrap SET status = ? WHERE user_id = ? AND post_id = ?";
        jdbcTemplate.update(sql, status.toString(), userId, postId);
    }
    public void addPostScrap(Long userId, Long postId) {
        String sql = "INSERT INTO scrap (user_id, post_id, status) VALUES (?, ?, 'ACTIVE')";
        jdbcTemplate.update(sql, userId, postId);
    }
    public void increasePostScrapCount(Long postId) {
        String sql = "UPDATE post SET count_scrap = count_scrap + 1 WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }
    public void decreasePostScrapCount(Long postId) {
        String sql = "UPDATE post SET count_scrap = count_scrap - 1 WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

    public List<PostSummaryDto> findAllPosts() {
        String sql = "SELECT p.post_id AS postId, p.topic, p.contents AS contentsSnippet, " +
                "p.count_like AS countLike, p.count_comment AS countComment, p.img_url AS imgUrl, " +
                "u.nickname AS authorName, p.count_scrap AS countScrap " +
                "FROM post p " +
                "JOIN user u ON p.user_id = u.user_id " +
                "WHERE p.status = 'ACTIVE'";

        List<PostSummaryDto> postSummaryList = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long postId = rs.getLong("postId");
            String topic = rs.getString("topic");
            String contentsSnippet = rs.getString("contentsSnippet");
            int countLike = rs.getInt("countLike");
            int countComment = rs.getInt("countComment");
            String imgUrl = rs.getString("imgUrl");
            String authorName = rs.getString("authorName");
            int countScrap = rs.getInt("countScrap");

            return new PostSummaryDto(postId, topic, contentsSnippet, countLike, countComment, imgUrl, authorName, countScrap);
        });

        return postSummaryList;
    }

    public PostInfoDto getPostInfo(Long userId, Long postId) {
        String sql = "SELECT p.post_id, p.topic, p.contents, p.count_like, " +
                "p.count_comment, p.count_scrap, p.img_url, " +
                "u.nickname, p.created_at, u.profile_img, " +
                "COALESCE(pl.user_id IS NOT NULL, FALSE) AS is_my_like, " +
                "COALESCE(pc.user_id IS NOT NULL, FALSE) AS is_my_scrap " +
                "FROM post p " +
                "JOIN user u ON p.user_id = u.user_id " +
                "LEFT JOIN post_like pl ON p.post_id = pl.post_id AND pl.user_id = ? AND pl.status = 'ACTIVE' " +
                "LEFT JOIN scrap pc ON p.post_id = pc.post_id AND pc.user_id = ? AND pc.status = 'ACTIVE' " +
                "WHERE p.post_id = ? AND p.status = 'ACTIVE'";

        return jdbcTemplate.queryForObject(sql, new Object[]{userId, userId, postId}, (rs, rowNum) -> {
            Long postIdResult = rs.getLong("p.post_id");
            String topic = rs.getString("topic");
            String contents = rs.getString("contents");
            int countLike = rs.getInt("count_like");
            int countComment = rs.getInt("count_comment");
            int countScrap = rs.getInt("count_scrap");
            String postImgUrl = rs.getString("p.img_url");
            String authorName = rs.getString("nickname");
            LocalDateTime createdAt = rs.getObject("p.created_at", LocalDateTime.class);
            String userImgUrl = rs.getString("u.profile_img");
            Boolean isMyLike = rs.getBoolean("is_my_like");
            Boolean isMyScrap = rs.getBoolean("is_my_scrap");

            return new PostInfoDto(postIdResult, authorName, createdAt, userImgUrl, topic, contents, countLike, countComment, countScrap, postImgUrl, isMyLike, isMyScrap);
        });
    }

    public List<CommentInfoDto> getCommentInfo(Long userId, Long postId) {

        String sql = "SELECT \n" +
                "    c.comment_id,\n" +
                "    CASE \n" +
                "        WHEN c.status = 'INACTIVE' THEN '삭제된 메시지입니다'\n" +
                "        ELSE c.contents\n" +
                "    END AS contents1,\n" +
                "    c.count_like,\n" +
                "    c.created_at,\n" +
                "    c.group_number,\n" +
                "    c.comment_level,\n" +
                "    u.user_id,\n" +
                "    CASE \n" +
                "        WHEN c.status = 'INACTIVE' THEN '익명'\n" +
                "        ELSE u.nickname\n" +
                "    END AS nickname1,\n" +
                "    u.profile_img,\n" +
                "    COALESCE(cl.user_id IS NOT NULL, FALSE) AS is_my_like\n" +
                "FROM \n" +
                "    comment c\n" +
                "JOIN \n" +
                "    user u ON c.user_id = u.user_id\n" +
                "LEFT JOIN \n" +
                "    comment_like cl ON c.comment_id = cl.comment_id AND cl.user_id = ? AND cl.status = 'ACTIVE'\n" +
                "WHERE \n" +
                "    c.post_id = ?\n" +
                "ORDER BY \n" +
                "    c.group_number ASC,\n" +
                "    c.comment_level ASC,\n" +
                "    c.created_at ASC;";

        // userId와 postId를 파라미터로 전달하여 PreparedStatement에 바인딩
        List<CommentInfoDto> commentInfoDtoList = jdbcTemplate.query(sql, new Object[]{userId, postId}, (rs, rowNum) -> {
            String contents = rs.getString("contents1");
            Integer countLike = rs.getInt("c.count_like");
            LocalDateTime createdAt = rs.getObject("c.created_at", LocalDateTime.class);
            Long userId2 = rs.getLong("u.user_id");
            String userNickname = rs.getString("nickname1");
            String userImgUrl = rs.getString("u.profile_img");
            Boolean isMyLike = rs.getBoolean("is_my_like");
            Integer commentLevel = rs.getInt("comment_level");
            Integer groupNumber = rs.getInt("group_number");

            return new CommentInfoDto(userId2, userNickname, userImgUrl, contents, countLike, isMyLike, createdAt, commentLevel, groupNumber);
        });

        return commentInfoDtoList;
    }

    public void createComment(Long userId, Long postId, CommentRequestDto commentRequestDto) {

        //group number는 최초 댓글일 경우에는 1로, 최초 댓글이 아닐 경우는 group_number의 값들 중 (가장 큰 값 + 1) 로 할당
        String sql = "INSERT INTO comment (group_number, contents, parent_comment_id, comment_level,  count_like, user_id, post_id, status) " +
                "VALUES (if((select counting from(select count(*) as counting from comment where post_id = ?) c) = 0, 1, ((select num + 1  from (select max(group_number) as num from comment where post_id = ?) as A) ) ), ?, 0, 1, 0, ?, ?, 'ACTIVE')";


        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[] {"comment_id"});
                    ps.setLong(1, postId);
                    ps.setLong(2, postId);
                    ps.setString(3, commentRequestDto.getContents());
                    ps.setLong(4, userId);
                    ps.setLong(5, postId);
                    return ps;
                },
                keyHolder
        );

    }

    public void increaseCountComment(Long post_id) {
        this.jdbcTemplate.update(
                "update post set count_comment = count_comment + 1 where post_id = ?",
                post_id
        );
    }

    public String checkPostStatus(Long post_id) {
        return this.jdbcTemplate.queryForObject(
                "select status from post where post_id = ?;",
                String.class, post_id
        );
    }

    public void createReplyComment(Long userId, Long postId, Long parentCommentId, CommentRequestDto commentRequestDto) {

        String sql = "insert into comment(group_number, comment_level, count_like, parent_comment_id, post_id, user_id, contents, status)" +
                "values((select groupNum from (select distinct group_number as groupNum from comment where comment_id = ?) as c), 2 , 0,  ? , ? , ? ,?, 'ACTIVE')";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[] {"comment_id"});
                    ps.setLong(1, parentCommentId);
                    ps.setLong(2, parentCommentId);
                    ps.setLong(3, postId);
                    ps.setLong(4, userId);
                    ps.setString(5, commentRequestDto.getContents());
                    return ps;
                },
                keyHolder
        );

    }

    public String checkCommentStatus(Long commentId) {
        return this.jdbcTemplate.queryForObject(
                "select status from comment where comment_id = ?;",
                String.class, commentId
        );

    }

    public Long findUserIdByCommentId(Long commentId){
        return this.jdbcTemplate.queryForObject(
                "select user_id from comment where comment_id = ?;",
                Long.class, commentId
        );

    }

    public void updateComment(Long userId, Long commentId, UpdateCommentDto updateCommentDto) {

        String sql =  "UPDATE comment SET contents = ? WHERE comment_id = ? AND user_id = ?";

        jdbcTemplate.update(sql,
                updateCommentDto.getContents(),
                commentId,
                userId);
    }

    public boolean testPostId(Long postId) {
        int rowCount = this.jdbcTemplate.queryForObject("select count(*) from post where post_id = ? and status = 'ACTIVE'", Integer.class, postId);
        if(rowCount == 0){
            return false;
        }
        else{
            return true;
        }

    }

//    // 최대 groupNumber 가져오기
//    public int getMaxGroupNumber(Long postId) {
//        String sql = "SELECT COALESCE(MAX(group_number), 0) FROM comment WHERE post_id = ?";
//        return jdbcTemplate.queryForObject(sql, Integer.class, postId);
//    }
//
//    // 부모 댓글의 commentLevel 가져오기
//    public int getCommentLevel(Long parentCommentId) {
//        String sql = "SELECT comment_level FROM comment WHERE comment_id = ?";
//        return jdbcTemplate.queryForObject(sql, Integer.class, parentCommentId);
//    }
//
//    // 부모 댓글의 groupNumber 가져오기
//    public int getGroupNumber(Long parentCommentId) {
//        String sql = "SELECT group_number FROM comment WHERE comment_id = ?";
//        return jdbcTemplate.queryForObject(sql, Integer.class, parentCommentId);
//    }
//
//    // 댓글 삽입
//    public void insertComment(Long userId, Long postId, CommentRequestDto commentRequestDto, int groupNumber, int commentLevel, Long parentCommentId) {
//        String sql = "INSERT INTO comment (contents, parent_comment_id, comment_level, group_number, count_like, user_id, post_id, status) " +
//                "VALUES (?, ?, ?, ?, 0, ?, ?, 'ACTIVE')";
//        jdbcTemplate.update(sql, commentRequestDto.getContents(), parentCommentId, commentLevel, groupNumber, userId, postId);
//    }

//    public Long createComment(Long userId, Long postId, CommentRequestDto commentRequestDto) {
//        String sql = "INSERT INTO comment (contents, parent_comment_id, comment_level, group_number, count_like, user_id, post_id, status) " +
//                "VALUES (?, NULL, 1, 1, 0, ?, ?, 'ACTIVE')";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(
//                con -> {
//                    PreparedStatement ps = con.prepareStatement(sql, new String[] {"comment_id"});
//                    ps.setString(1, commentRequestDto.getContents());
//                    ps.setLong(2, userId);
//                    ps.setLong(3, postId);
//                    return ps;
//                },
//                keyHolder
//        );
//
//        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
//    }
//
//    public Long createReplyComment(Long userId, Long postId, Long parentId, CommentRequestDto commentRequestDto) {
//        String sql = "INSERT INTO comment (contents, parent_comment_id, comment_level, group_number, count_like, user_id, post_id, status) " +
//                "VALUES (?, ?, 2, (SELECT group_number FROM comment WHERE comment_id = ?), 0, ?, ?, 'ACTIVE')";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(
//                con -> {
//                    PreparedStatement ps = con.prepareStatement(sql, new String[] {"comment_id"});
//                    ps.setString(1, commentRequestDto.getContents());
//                    ps.setLong(2, parentId); // Set parent comment ID for reply
//                    ps.setLong(3, parentId); // Use parent comment ID to get group number
//                    ps.setLong(4, userId);
//                    ps.setLong(5, postId);
//                    return ps;
//                },
//                keyHolder
//        );
//
//        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
//    }
//
//
//    public boolean isCommentById(Long commentId) {
//        String sql = "SELECT COUNT(*) FROM comment WHERE comment_id = ? AND status = 'ACTIVE'";
//        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{commentId}, Integer.class);
//        return count != null && count > 0;
//    }
}
