package support.auth.authorization;

import java.util.Objects;
import nextstep.member.domain.Guest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;
import support.auth.userdetails.User;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return new User(authentication.getPrincipal().toString(), null, authentication.getAuthorities());
        }

        AuthenticationPrincipal annotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        if (Objects.requireNonNull(annotation).required()) {
            throw new IllegalStateException("Authentication is required");
        }

        return Guest.guestUser;
    }
}
