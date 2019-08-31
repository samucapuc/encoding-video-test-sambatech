package br.com.samuel.sambatech.validators;

import java.util.Arrays;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import br.com.samuel.sambatech.annotations.ValidVideo;
import br.com.samuel.sambatech.utils.FileUtils;
import br.com.samuel.sambatech.utils.MessageUtils;

@Component
public class VideoFileValidator implements ConstraintValidator<ValidVideo, MultipartFile> {

  @Autowired
  private MessageUtils messageUtils;

  @Override
  public void initialize(ValidVideo constraintAnnotation) {

  }

  @Override
  public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
    if (!isSupportedFileExtension(multipartFile.getOriginalFilename())) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              messageUtils.getMessage("msg.invalid.extension") + Arrays
                  .asList(FileUtils.extensionSupport).stream().collect(Collectors.joining(", ")))
          .addConstraintViolation();
      return false;
    }
    return true;
  }

  private boolean isSupportedFileExtension(String originalNameFile) {
    String extension = originalNameFile
        .substring(originalNameFile.lastIndexOf("."), originalNameFile.length()).toUpperCase();
    return Arrays.asList(FileUtils.extensionSupport).contains(extension);
  }
}
