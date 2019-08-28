package br.com.samuel.sambatech.utils;

public class Utils {

  public static String changeExtensionFile(String originalNameFile, String sufix,
      String newExtension) {
    return originalNameFile.substring(0, originalNameFile.lastIndexOf(".")).concat(sufix)
        .concat(newExtension);

  }
}
