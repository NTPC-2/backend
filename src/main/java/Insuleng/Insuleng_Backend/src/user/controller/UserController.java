package Insuleng.Insuleng_Backend.src.user.controller;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponse;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.src.storage.S3Uploader;
import Insuleng.Insuleng_Backend.src.user.dto.*;
import Insuleng.Insuleng_Backend.src.user.service.AuthService;
import Insuleng.Insuleng_Backend.src.user.service.UserService;
import Insuleng.Insuleng_Backend.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    private final S3Uploader s3Uploader;

    @GetMapping("profiles")
    @Operation(summary = "마이페이지 api", description = "즐겨찾기한 음식점, 내가 쓴 리뷰, 좋아요 누른 음식점, 내가 쓴 글, 스크랩한 글의 개수가 출력", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    //추후 bookmark, review, heart, scrap 개수가 출력되는지 테스트해보기
    public BaseResponse<MyPageDto> getMyPage(){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            MyPageDto myPageDto = userService.getMyPage(userId);

            return new BaseResponse<>(myPageDto);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    @GetMapping("profiles/update")
    @Operation(summary = "정보 수정 폼을 제공하는 api", description = "내 개인정보를 보여주며, 이곳에서 정보를 수정가능합니다", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<MyPageFormDto> getMyPageInfo(){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            MyPageFormDto myPageFormDto = userService.getMyPageInfo(userId);

            return new BaseResponse<>(myPageFormDto);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PutMapping(value = "profiles/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "정보를 실제로 수정하는 api", description = "MyPageUpdateDto(닉네임, 전화번호, 성별, 나이, 프로필 사진)에 담긴 내용으로 내 정보를 수정", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "3011", description = "이미 존재하는 닉네임입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<String> updateMyPageInfo(@Valid MyPageUpdateDto myPageUpdateDto){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            userService.updateMyPageInfo(userId, myPageUpdateDto);

            return new BaseResponse<>("내 정보를 수정했습니다");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    @GetMapping("profiles/bookmark")
    @Operation(summary = "내가 즐겨찾기한 음식점을 보여주는 api", description = "List<MyBookmarkDto> 이용", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<List<MyBookmarkDto>> getMyBookmarks(){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            List<MyBookmarkDto> bookmarkDtoList = userService.getMyBookmarks(userId);

            return new BaseResponse<>(bookmarkDtoList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("profiles/post")
    @Operation(summary = "내가 작성한 글을 보여주는 api", description = "List<MyPostDto> 이용", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<List<MyPostDto>> getMyPosts(){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            List<MyPostDto> postDtoList = userService.getMyPosts(userId);

            return new BaseResponse<>(postDtoList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("profiles/heart")
    @Operation(summary = "내가 좋아요한 음식점을 보여주는 api", description = "List<MyHeartDto> 이용", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<List<MyHeartDto>> getMyHearts(){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            List<MyHeartDto> heartDtoList = userService.getMyHearts(userId);

            return new BaseResponse<>(heartDtoList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("profiles/review")
    @Operation(summary = "내가 작성한 리뷰를 보여주는 api", description = "List<MyReviewDto> 이용", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<List<MyReviewDto>> getMyReviews(){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            List<MyReviewDto> reviewDtoList = userService.getMyReviews(userId);

            return new BaseResponse<>(reviewDtoList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }


    }

    @GetMapping("profiles/scrap")
    @Operation(summary = "내가 스크랩한 글을 보여주는 api", description = "List<MyScrapDto> 이용", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
            @ApiResponse(responseCode = "2360", description = "로그인이 필요한 서비스입니다")
    })
    public BaseResponse<List<MyScrapDto>> getMyScraps(){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            List<MyScrapDto> scrapDtoList = userService.getMyScraps(userId);
            return new BaseResponse<>(scrapDtoList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PatchMapping("profiles/delete")
    @Operation(summary = "회원 탈퇴 api", description = "user의 stauts를 INACTIVE로 변경", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
    })
    public BaseResponse<String> deleteUser(){
        try{
            Long userId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            userService.deleteUser(userId);
            return new BaseResponse<>("유저를 삭제했습니다");

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }


    @PatchMapping(value = "{user_id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "s3 업로드를 위해 만든 연습용 api", description = "이미지 업로드 용", responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "2005", description = "존재하지 않은 유저입니다"),
    })
    public BaseResponse<String> updateMyImage(@PathVariable("user_id")Long userId, @RequestBody MultipartFile file){
        try{
            userService.imageUpload(userId, file);

            return new BaseResponse<>("이미지가 저장 되었습니다");

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }


}
