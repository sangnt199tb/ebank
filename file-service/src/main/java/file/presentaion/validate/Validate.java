package file.presentaion.validate;

import file.presentaion.exception.ErrorCode;
import file.presentaion.exception.FileException;
import org.apache.commons.io.FilenameUtils;

import java.util.Arrays;
import java.util.Locale;

public class Validate {
    private static final Character[] INVALID_CHECK_FILE_NAME_SPECIFIC_CHARS = {'"', '%', '<', '>', '&', '\\', '+'};
    private static String[] validateFileType = new String[]{"PNG", "JPG", "JPEG", "PDF", "MSG"};

    public static void validateFileName(String fileName){
        Boolean result = Arrays.stream(INVALID_CHECK_FILE_NAME_SPECIFIC_CHARS)
                .noneMatch(ch -> fileName.contains(ch.toString()));
        if(!result){
            throw new FileException(ErrorCode.FILE_NAME_ERROR);
        }
    }

    public static void validateFileType(String fileName){
        Boolean result = Arrays.stream(validateFileType).anyMatch(getFileExtension(fileName)::equals);
        if(!result){
            throw new FileException(ErrorCode.FILE_TYPE_ERROR);
        }
    }

    public static String getFileExtension(String fileName){
        return FilenameUtils.getExtension(fileName).toUpperCase(Locale.ROOT);
    }
}
