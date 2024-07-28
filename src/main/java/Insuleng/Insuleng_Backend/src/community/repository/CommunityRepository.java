package Insuleng.Insuleng_Backend.src.community.repository;

import Insuleng.Insuleng_Backend.src.community.dto.PostDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.temporal.Temporal;

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
//        Long postId = keyHolder.getKey().longValue(); //프라이머리 키 구함

    }
    public boolean testUserId(Long userId){
//        String sql = this.jdbcTemplate.queryForObject("select exists (select user_id from post where user_id = ?", String.class, userId);
//        int count = jdbcTemplate.queryForObject("SELECT * FROM MyTable WHERE Param = ?", new Object[] {myParam}, String.class);
//        if(count == 0)

        int rowCount = this.jdbcTemplate.queryForObject("select count(*) from user where user_id = ?", Integer.class, userId);
        if(rowCount == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public void deletePost(Long postId){
        String sql = "DELETE FROM post WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }
    public boolean findPostById(Long postId){
        String sql = "SELECT COUNT(*) FROM post WHERE post_id = ?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, postId);
        if(rowCount == 0){
            return false;
        }
        else{
            return true;
        }
    }
}
