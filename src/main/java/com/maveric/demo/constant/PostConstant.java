package com.maveric.demo.constant;

public final class PostConstant {

    private PostConstant() {
        // restrict instantiation
    }

    public static final String DELETEPOST = "Post deleted for "  ;
    public static final String POSTNOTFOUND = "Post not found for : ";
    public static final String POSTIDMISMATCH = "Check the PostId ,Post Id Mismatch found";
    public static final String FEIGNEXCEPTON= "Service connectivity issue to be fixed on one of the services you are requesting";

}
