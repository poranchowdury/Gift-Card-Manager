package com.gft.manager.util;

public final class UrlConstrains {
    private UrlConstrains() { }
    private static final String API = "/api";
    private static  final String VERSION = "/v1";
   public static class FileManagement{
       private  FileManagement() { }
       public static final String ROOT = API+VERSION+"/image";
       public static final String UPLOAD = "/upload";
       public static  final String FIND_BY_ID= "/{id}";
   }
   public static class UserManagement {
       private UserManagement() { }
       public static final String ROOT = API+VERSION+"/signup";
       public static final String ADMIN_SIGNUP = "/admin";
       public static final String REVIEWER_SIGNUP = "/reviewer";
       public static final String INFLUENCER_SIGNUP = "/influencer";
   }


    public class LogIn {
        private LogIn() {
        }
        public static final String ROOT = API+VERSION;
        public static final String LOGIN = "/login";

    }

    public static final class ProfileManagement {
        public static final String ROOT = API+VERSION+"/profile";
        public static final String FIND_BY_USER_NAME= "/{userEmail}";
        public static  final String CREATE_PROFILE = "/{userEmail}";
        public static final String FIND_REVIEWER = "/" ;
        public static final String UPDATE = "/update/{userEmail}";
    }

    public class ReleventQs {
        public static final String ROOT = API+VERSION+"/questions";
        public static final String All ="/";
    }

    public static final class UploadConfig {
        public static final String ROOT = API+VERSION+"/config";
        public static final String GET = "/";


        public static final String CREATE = "/";
    }

    public static final class MetadataImageUpload {
        public static final String ROOT = API+VERSION+"/image";
    }

    public static final class VideoIdGenerate {
        public static final String ROOT = API+VERSION+"/videoId";
        public static  final String GENERATE ="/{numberOfId}";
    }

    public static final class VideoMetaDataManagement {
        public static final String ROOT = API+VERSION+"/metadata";

        public static  final String CREATE ="/";
        public static  final String UPDATE ="/{videoId}";
        public static  final String RE_UPDATE ="/reUpload/{videoId}";
        public static  final String GET_BY_ID ="/{id}";
        public static  final String GET_BY_ID_REVIEW ="/review/{id}";
        public static  final String GET_BY_EMAIL ="/video/{email}";
    }

    public static final class VideoUpload {
        public static final String ROOT = API+VERSION+"/video";

        public static  final String CREATE ="/upload/{videoId}/{userEmail}";
        public static  final String GET_BY_ID ="/{name}";

        public static final String REUPLOAD = "/reUpload/{videoId}/{userEmail}";
    }

    public static final class ReviewManagement {
        public static final String ROOT = API+VERSION+"/review";

        public static final String ASSIGN = "/assign";
        public static final String VIDEOS = "/videos/{userEmail}";
        public static final String VIDEOSPOST = "/videos";
        public static final String ON_REVIEW = "/{videoId}";
        public static final String VIDOMETADATA = "/metadata/{videoId}";
    }
}
