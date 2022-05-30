package com.linkpcom.mitrafast.Classes.Others;

public class CONSTANTS {

    public static class API {

        //TEST
//        public static String BASE_URL = "http://81.10.36.145/vt/vt_speedapp_web/api/";

        //LIVE
        public static String BASE_URL = "https://speedsapp.com/admin/api/";

    }

    public static class INTENTS {
        public static final String NAME = "NAME";
        public static final String OBJECT = "object";
        public static final String NAME_CARD = "NameCard";
        public static final String ID = "ID";
        public static final String LATLNG = "latlng";
        public static final String ADDRESS = "address";
        public static final String AGENT_ID = "AGENT_ID";
        public static final String MOBILE_PHONE_NUMBER = "mobile phone_number";
        public static final String DATE = "DATE";
        public static final String PRODUCT_ID = "PRODUCT_ID";
        public static final String REQUEST_CODE = "REQUEST_CODE";
        public static final String IS_WAITING = "IS_WAITING";
        public static final String STATUS_ORDER = "Status_Order";
        public static final String NOTIFICATIONS = "Notification";

        public static final int VERIFY_USER = 1;
        public static final int PICK_SHOP_LOCATION = 2;
        public static final int REQUEST_CHECK_SETTINGS = 3;
        public static final int SELECT_DELIVERY_LOCATION = 4;
        public static final int SELECT_PAYMENT_METHOD = 5;
        public static final int CREATE_FIRST_FAVORITE_PLACE = 6;
        public static final int CHANGE_FORGOTTEN_PASSWORD = 8;
        public static final int RATE_CLIENT = 9;
        public static final int RATE_AGENT = 10;
        public static final int PICK_LOCATION = 20;
        public static final int COMPLETE_DATA = 25;
        public static final int PICK_PROFILE_IMAGE_FOR_NAV = 10;
        public static final int PROFILE_PIC = 11;
        public static final int COMMERCIAL_REGISTER = 12;
        public static final int ID_PHOTO = 13;



    }

    public static class SHARED_PREFERENCES {
        /*
         ** Shared Preferences Constants
         */
        public static final String SHARED_PREFERENCES_NAME = "americanClinic";
        public static final String MOBILE_PHONE_NUMBER = "mobile phone_number";
        public static final String AUTHENTICATION_TOKEN = "Authentication_token";
        public static final String AUTHENTICATION_TYPE = "Authentication_type";
        public static final String PHONE_START_WITH = "phone_start_with";
        public static final String PHONE_NUMBER_COUNT = "phone_number_count";
        public static final String PHONE_NUMBER_CODE = "phone_number_code";
        public static final String USER_NAME = "user_name";
        public static final String EMAIL = "email";
        public static final String IS_USER_VERIFIED = "is_user_verified";
        public static final String IMAGE = "image";
        public static final String CURRENCY_SYMBOL = "currency_symbol";
        public static final String LANGUAGE = "language";
        public static final String ORDER_ID = "order_id";
        public static final String ORDER_TYPE = "order_type";
        public static final String USER_ID = "userId";
        public static final String USER_TYPE_ID = "userTypeId";
        public static final String IS_PROFILE_COMPLETED = "IsProfileCompleted";
        public static final String IS_SERVICES_COMPLETED = "IsServiceCompleted";
        public static final String IS_AVAILABLE = "IsAvailable";
        public static final String SECTIONS = "sections";
        public static final String IS_BUSY = "IS_BUSY";
        public static final String IS_RESET_PASSWORD = "IS_RESET_PASSWORD";
        public static final String IS_AUTH_USER = "is auth user";
        public static final String LAT = "LAT";
        public static final String LNG = "LNG";
        public static final String ADDRESS = "address";
        public static final String ADDRESS_NAME = "ADDRESS_NAME";
        public static final String ADDRESS_ID = "ADDRESS_ID";
        public static final String DELIVERY_SERVICE = "delivery_service";


    }
}
