package Insuleng.Insuleng_Backend.src.community.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.community.dto.PostDto;
import Insuleng.Insuleng_Backend.src.community.dto.PostSummaryDto;
import Insuleng.Insuleng_Backend.src.community.dto.UpdatePostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CommunityRepository {
    private final JdbcTemplate jdbcTemplate;

    public CommunityRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createPost(Long userId, PostDto postDto){
        String sql = "INSERT INTO post(count_comment, count_like, count_parent_comment, user_id, contents, img_url, topic, status)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"post_id"});
            ps.setInt(1, 0);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setLong(4, userId);
            ps.setString(5, postDto.getContents());
            ps.setString(6, postDto.getImgUrl());
            ps.setString(7, postDto.getTopic());
            ps.setString(8, "ACTIVE");
            return ps;
        }, keyHolder);
    }
    public boolean testUserId(Long userId){

        int rowCount = this.jdbcTemplate.queryForObject("select count(*) from user where user_id = ?", Integer.class, userId);
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

    public void updatePost(Long userId, UpdatePostDto updatePostDto){
        String sql =  "UPDATE post SET topic = ?, contents = ?, img_url = ? WHERE post_id = ? AND user_id = ?";

        jdbcTemplate.update(sql,
                updatePostDto.getTopic(),
                updatePostDto.getContents(),
                updatePostDto.getImgUrl(),
                updatePostDto.getPostId(),
                userId);

    }
    public List<PostSummaryDto> searchPosts(Long userId, String keyword) {
        String sql = "SELECT p.post_id, p.topic, p.contents, p.count_like, p.count_comment, p.img_url, u.nickname " +
                "FROM post p " +
                "JOIN user u ON p.user_id = u.user_id " +
                "WHERE (p.topic LIKE ? OR p.contents LIKE ?) AND p.status = 'ACTIVE'";


        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%", "%" + keyword + "%"}, (rs, rowNum) -> {
            Long postId = rs.getLong("post_id");
            String topic = rs.getString("topic");
            String contents = rs.getString("contents");
            int countLike = rs.getInt("count_like");
            int countComment = rs.getInt("count_comment");
            String imgUrl = rs.getString("img_url");
            String authorName = rs.getString("nickname");

            String contentsSnippet = getSnippet(contents);

            return new PostSummaryDto(postId, topic, contentsSnippet, countLike, countComment, imgUrl, authorName);
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
}
