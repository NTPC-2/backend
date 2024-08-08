package Insuleng.Insuleng_Backend.src.community.repository;

import Insuleng.Insuleng_Backend.src.community.dto.PostDto;
import Insuleng.Insuleng_Backend.src.community.dto.SearchPostDto;
import Insuleng.Insuleng_Backend.src.community.dto.UpdatePostDto;
import Insuleng.Insuleng_Backend.src.community.entity.PostEntity;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.Temporal;
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
    public List<PostEntity> searchPosts(SearchPostDto postSearchDto) {
        String sql = "SELECT * FROM post WHERE status = 'ACTIVE' AND (topic LIKE ? OR contents LIKE ?)";
        String searchPattern = "%" + postSearchDto.getKeyword() + "%";
        return jdbcTemplate.query(sql, new Object[]{searchPattern, searchPattern}, postRowMapper());
    }

    private RowMapper<PostEntity> postRowMapper() {
        return (rs, rowNum) -> {

            PostEntity post = new PostEntity();
            post.setPostId(rs.getLong("post_id"));
            //post.setUserId(rs.getLong("user_id"));
            post.setContents(rs.getString("contents"));
            post.setImgUrl(rs.getString("img_url"));
            post.setTopic(rs.getString("topic"));
            post.setCountComment(rs.getInt("count_comment"));
            post.setCountLike(rs.getInt("count_like"));
            post.setCountParentComment(rs.getInt("count_parent_comment"));


            // user_id를 사용하여 UserEntity 조회
            Long userId = rs.getLong("user_id");
            String userSql = "SELECT * FROM user WHERE user_id = ?";
            UserEntity user = jdbcTemplate.queryForObject(userSql, new Object[]{userId}, new RowMapper<UserEntity>() {
                @Override
                public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                    UserEntity user = new UserEntity();
                    user.setUserId(rs.getLong("user_id"));
                    user.setNickname(rs.getString("nickname"));
                    // 추가적인 UserEntity 필드 매핑
                    return user;
                }
            });
            post.setUser(user);  // UserEntity를 PostEntity에 설정

            return post;
        };
    }
}
