package br.com.samuel.sambatech.utils;

import org.springframework.stereotype.Component;

@Component
public class FileUtils {

  public static final String[] extensionSupport = new String[] {".MP4", ".MKV", ".MOV", ".AVI",
      ".FLV", ".MPEG2", ".MXF", ".LXF", ".GXF", ".3GP", ".WebM", ".MPG", ".QUICKTIME"};

  public String changeExtensionFile(String originalNameFile, String sufix, String newExtension) {
    return originalNameFile.substring(0, originalNameFile.lastIndexOf(".")).concat(sufix)
        .concat(newExtension);
  }
}
