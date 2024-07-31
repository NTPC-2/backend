package Insuleng.Insuleng_Backend.src.user.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.src.user.dto.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Slf4j  // 로그 출력을 위한 롬복
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private final AuthService authService;

    public String sendMail(EmailMessage emailMessage){
        String temporalPwd = createCode();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(temporalPwd, false);
            javaMailSender.send(mimeMessage);

            log.info("send Success!");
        }catch (MessagingException e){
            log.info("send Fail..  " + e.getMessage());
            throw new BaseException(BaseResponseStatus.FAIL_EMAIL_SEND);
        }
        authService.setTemporalPwd(emailMessage.getTo(), temporalPwd);
        return temporalPwd;
    }

    public String sendMail2(EmailMessage emailMessage){
        String temporalPwd = createCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(emailMessage.getSubject());
        message.setTo(emailMessage.getTo());
        message.setText(temporalPwd);

        javaMailSender.send(message);

        authService.setTemporalPwd(emailMessage.getTo(), temporalPwd);
        return temporalPwd;
    }

    public String createCode(){
         String[] randomList = new String[]{"~", "!", "@", "#", "$", "%", "^", "&", "*", "_", "-","/"
        ,"A","B","C","D","E","F","G","H","I","J","K","L", "M", "N", "O", "P", "Q", "S", "T", "U", "V", "W", "X", "Y", "Z"
        ,"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t","u", "v", "w", "x","y","z"};

        Random random = new Random(System.currentTimeMillis()+1920172);

        String temporalPwd = "";

        //랜덤 비밀번호는 10자리로 지정
        for(int i =0; i<10; i++){
            int ranNum = random.nextInt(64);

            temporalPwd += randomList[ranNum];
        }

        return temporalPwd;
    }

}
