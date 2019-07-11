package com.example.parsetagram5;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ParseComment")
public class ParseComment extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String POST = "post";

    public ParseFile getMedia() {
        return getParseFile("media");
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getDate() {
        return String.valueOf(getCreatedAt());
    }

    public ParseObject getPost() {
        return getParseObject(POST);
    }

    public void setPost(Post post) {
        put("post", post);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }

}
