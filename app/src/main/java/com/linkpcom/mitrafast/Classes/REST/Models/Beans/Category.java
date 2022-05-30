package com.linkpcom.mitrafast.Classes.REST.Models.Beans;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class Category {
    private int id;
    private String image;
    private String name;
    private CategoryBody category;

    @Setter
    @Getter
    public static class CategoryBody{
        private int id;
        private String image;
        private String name;
    }
}
