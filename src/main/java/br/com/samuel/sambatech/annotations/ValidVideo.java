package br.com.samuel.sambatech.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import br.com.samuel.sambatech.validators.VideoFileValidator;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {VideoFileValidator.class})
public @interface ValidVideo {
  String message() default "Invalid video file";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
