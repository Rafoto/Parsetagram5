package com.example.parsetagram5;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String DATE = "createdAt";
    public static final String USERS_WHO_LIKED = "userWhoLiked";

    public ParseFile getMedia() {
        return getParseFile("media");
    }

    public void setMedia(ParseFile parseFile) {
        put("media", parseFile);
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

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }

    public void addLike(ParseUser user) {

    }

    public void setRelation(ParseRelation relation) {
        put("likes", relation);
    }
}
