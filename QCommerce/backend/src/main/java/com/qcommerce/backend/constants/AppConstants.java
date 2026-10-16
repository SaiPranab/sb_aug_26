package com.qcommerce.backend.constants;

import java.io.File;

public class AppConstants {
    private AppConstants() {}

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";

    public static final String UPLOAD_DIR_CATEGORIES = "uploads" + File.separator + "category";
    public static final String UPLOAD_DIR_ITEMS = "uploads" + File.separator + "item";
    public static final String UPLOAD_DIR_PROFILE_IMAGE = "uploads" + File.separator + "profile_image";

    public static final String ALLOWED_IMAGE_TYPES[] = {"jpg", "jpeg", "png"};
}
