package com.dddev.market.place.utils;

/**
 * Created by ugar on 17.02.16.
 */
public class StaticKeys {
    public static final long TIME_UPDATE = 3*60*1000; // 3min
    public final static String CROP_IMAGE_URI = "crop_image_url";
    public final static int CROUP_REQUEST_CODE = 1;
    public final static int ATTACH_IMAGE_REQUEST_CODE = 2;
    public final static int MAKE_PHOTO_REQUEST_CODE = 3;
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String REQUEST_START = "request_start";
    public static final String REQUEST_CHECK = "request_check";
    public static final String KEY_REQUEST = "key_request";
    public static final String KEY_MESSAGE = "key_message";
    public static final String ACCEPT_BIDS_ID = "accept_bids_id";
    public static final String COMPLETE_BIDS_ID = "complete_bids_id";

    public class LoaderId {
        public static final int TAB_LOADER = 1;
        public static final int BIDS_LOADER = 2;
        public static final int CATEGORY_LOADER = 3;
        public static final int ALL_BIDS_LOADER = 4;
        public static final int OPPORTUNITIES_LOADER = 5;
        public static final int ACCEPT_STATE_LOADER = 6;
    }
}
