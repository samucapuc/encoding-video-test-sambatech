package br.com.samuel.sambatech.utils;

import org.springframework.stereotype.Component;

@Component
public class FileUtils {

  public String changeExtensionFile(String originalNameFile, String sufix, String newExtension) {
    return originalNameFile.substring(0, originalNameFile.lastIndexOf(".")).concat(sufix)
        .concat(newExtension);

  }
}
