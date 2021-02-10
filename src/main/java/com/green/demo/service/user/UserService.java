package com.green.demo.service.user;

import com.green.demo.configure.AppProperties;
import com.green.demo.error.NotFoundException;
import com.green.demo.mail.EmailMessage;
import com.green.demo.mail.EmailService;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import com.green.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final TemplateEngine templateEngine;
  private final UserRepository userRepository;
  private final AppProperties appProperties;
  private final EmailService emailService;


  public UserService(PasswordEncoder passwordEncoder, TemplateEngine templateEngine, UserRepository userRepository, AppProperties appProperties, EmailService emailService) {
    this.passwordEncoder = passwordEncoder;
    this.templateEngine = templateEngine;
    this.userRepository = userRepository;
    this.appProperties = appProperties;
    this.emailService = emailService;
  }

  @Transactional
  public User join(String name, Email email, String password) {
    checkArgument(isNotEmpty(password), "password must be provided.");
    checkArgument(
      password.length() >= 4 && password.length() <= 15,
      "password length must be between 4 and 15 characters."
    );

    User user = new User(name, email, passwordEncoder.encode(password));
    user.generateEmailCheckToken();
    User saved = insert(user);

    return saved;
  }

  public void sendSignUpConfirmEmail(User newUser){
    Context context = new Context();
    context.setVariable("link", "/check-email-token?token="+ newUser.getEmailCheckToken() +
            "&email=" +newUser.getEmail());
    context.setVariable("name",newUser.getName());
    context.setVariable("linkName", "회원가입 이메일 인증");
    context.setVariable("message", "티켓원정대 회원 가입을 위해서 링크를 클릭하세요");
    context.setVariable("host", appProperties.getHost());
    String message = templateEngine.process("mail/simple-link", context);

    EmailMessage emailMessage = EmailMessage.builder()
            .to(String.valueOf(newUser.getEmail()))
            .subject("티켓 원정대, 회원 가입 인증")
            .message(message)
            .build();
    emailService.sendEmail(emailMessage);
  }

  @Transactional
  public User login(Email email, String password) {
    checkNotNull(password, "password must be provided.");

    User user = findByEmail(email)
      .orElseThrow(() -> new NotFoundException(User.class, email));
    user.login(passwordEncoder, password);
    user.afterLoginSuccess();
    update(user);
    return user;
  }

  @Transactional
  public User updateProfileImage(Long userId, String profileImageUrl) {
    User user = findById(userId)
      .orElseThrow(() -> new NotFoundException(User.class, userId));
    user.updateProfileImage(profileImageUrl);
    update(user);
    return user;
  }

  @Transactional(readOnly = true)
  public Optional<User> findById(Long userId) {
    checkNotNull(userId, "userId must be provided.");

    return userRepository.findById(userId);
  }

  @Transactional(readOnly = true)
  public Optional<User> findByEmail(Email email) {
    checkNotNull(email, "email must be provided.");

    return userRepository.findByEmail(email);
  }

  private User insert(User user) {
    return userRepository.save(user);
  }

  private void update(User user) {
    userRepository.save(user);
  }

}