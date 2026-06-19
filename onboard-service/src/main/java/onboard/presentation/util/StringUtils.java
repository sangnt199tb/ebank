package onboard.presentation.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {
    public static String removeAccentsAndSpaces(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noAccents = pattern.matcher(normalized).replaceAll("");
        return noAccents
                .replace("đ", "d")
                .replace("Đ", "D")
                .replaceAll("\\s+", "");
    }
}
