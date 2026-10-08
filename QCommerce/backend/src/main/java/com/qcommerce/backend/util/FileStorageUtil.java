package com.qcommerce.backend.util;

import com.qcommerce.backend.constants.AppConstants;
import com.qcommerce.backend.exception.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FileStorageUtil {
    private FileStorageUtil() {
    }

    public static void deleteFile(String fileName, String directory) {
//        if(fileName == null || fileName.isEmpty()) {
//            throw new
//        }
    }

    public static String saveImage(MultipartFile image, String directory) {
        // check the validity of MultiPartFile
        if (image == null || image.isEmpty()) {
            throw new InvalidFileException("Image not found");
        }

        String originalFileName = image.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new InvalidFileException("Invalid image file");
        }

        // Check if it is a valid or accepted image or not
        validateFileExtension(originalFileName);

        // create a new File name
        String newFileName = getNewFileName(originalFileName);

        try {

            // Save the image with the new file name
            File uploadDirectory = new File(directory);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdir();
            }

            FileOutputStream fos = new FileOutputStream(directory);
            fos.write(image.getBytes());
            fos.close();

            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidFileException("Unable to save image");
        }
    }

    private static void validateFileExtension(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        System.out.println("////" + extension);
        List<String> acceptedExtensions = Arrays.asList(AppConstants.ALLOWED_IMAGE_TYPES);
        if (!acceptedExtensions.contains(extension)) {
            throw new InvalidFileException("Invalid image type. Allowed: JPG, JPEG, PNG");
        }
    }

    private static String getNewFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newName = UUID.randomUUID().toString() + System.currentTimeMillis() + extension;
        return newName;
    }
}
