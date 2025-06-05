package com.app85soft.qiqishop.security.interceptor;

import com.app85soft.qiqishop.annotations.NoRequireAuth;
import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.user.UserRepository;
import com.app85soft.qiqishop.security.JwtTokenProvider;
import com.app85soft.qiqishop.security.SecurityContexts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Log4j2
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            NoRequireAuth methodAnnotation = handlerMethod.getMethodAnnotation(NoRequireAuth.class);
            NoRequireAuth classAnnotation = handlerMethod.getBeanType().getAnnotation(NoRequireAuth.class);
            if (methodAnnotation != null || classAnnotation != null) {
                return true;
            }
        }
        BusinessException exception = new BusinessException(Translator.toLocale("login_required"), HttpStatus.UNAUTHORIZED);
        String vendorCode = request.getHeader("Authorization");
        if (Strings.isEmpty(vendorCode)) {
            throw exception;
        }
        String[] header = vendorCode.split(" ");
        if (header.length != 2) {
            throw exception;
        }
        String token = header[1];
        if (jwtTokenProvider.validateTokenRs256(token)) {
            Integer sub = Integer.parseInt(jwtTokenProvider.getSubIdFromJwtRs256(token));
            SecurityContexts.newContext();
            User user = userRepository.findById(sub).orElseThrow(()-> exception);
            SecurityContexts.getContext().setData(user);
            return true;
        }
        throw exception;
    }
}
